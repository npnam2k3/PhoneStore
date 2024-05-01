package com.devbackend.web_telephone_ttcn.controller.user;

import com.devbackend.web_telephone_ttcn.dto.CartDTO;
import com.devbackend.web_telephone_ttcn.dto.CategoryDto;
import com.devbackend.web_telephone_ttcn.entity.*;
import com.devbackend.web_telephone_ttcn.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;

    @ModelAttribute
    public void addLoggedInUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
    }
    @PostMapping("/addToCart")
    public String addToCart(@RequestParam("idProductCart") String idStr, RedirectAttributes redirectAttributes, Model model){
        Long idProduct = -1L;
        try {
            idProduct = Long.parseLong(idStr);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        redirectAttributes.addAttribute("id", idProduct);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findUserByUsername(username);
        if(user == null){
            // neu chua dang nhap
            System.out.println("Bạn cần đăng nhập để thêm vào giỏ hàng ");
            Product product = productService.findById(idProduct);
            CategoryDto categoryDto = categoryService.findById(product.getCategory().getId());
            List<Comments> comments = commentService.getCommentsConfirmed(idProduct);

            model.addAttribute("product", product);
            model.addAttribute("category", categoryDto);
            model.addAttribute("listComment", comments);
//            redirectAttributes.addFlashAttribute("login", "Bạn cần đăng nhập để thêm vào giỏ hàng");
//            return "redirect:/productDetail";
            model.addAttribute("login", "Bạn cần đăng nhập để thêm vào giỏ hàng");
            return "user/productDetail";
        }else{
            // da dang nhap
            Long userId = user.getId();

            CartDTO cartDTO = new CartDTO();
            cartDTO.setQuantity(1);
            cartDTO.setProduct(productService.findById(idProduct));
            cartDTO.setTotalPrice(1*(productService.findById(idProduct).getPrice()));
//            System.out.println(cartDTO.getTotalPrice());
//            System.out.println(cartDTO.getProduct().getProductName());

            // kiem tra user co cart hay chua
            if(cartService.checkCartOfUser(userId)){
                // neu da co gio hang
                Cart cart = cartService.getCartOfUserByUserId(userId);

                System.out.println(" Đã có giỏ hàng ");
                if(cartService.checkCartItemExists(idProduct, cart.getId())){
//                    System.out.println("Tăng lên 1");
                    // lay cartItem da tồn tại
                    CartItem cartItem = cartService.getCartItemByProductId(idProduct, cart.getId());
                    if(cartService.updateCartItem(cartItem, 1, cart)){
                        System.out.println("Update thành công");
                        redirectAttributes.addFlashAttribute("addSuccess", "Đã thêm vào giỏ hàng");
                        return "redirect:/productDetail";
                    }
                }else {
                    System.out.println("Thêm mới cartItem ");
                    Cart cartNew = cartService.findByUserId(userId);
                    if(cartService.addCartItemToCart(cartDTO, cartNew)){
                        System.out.println("Đã thêm vào giỏ hàng");
                        redirectAttributes.addFlashAttribute("addSuccess", "Đã thêm vào giỏ hàng");
                        return "redirect:/productDetail";
                    }else {
                        System.out.println("Không thể thêm vào giỏ hàng");
                        redirectAttributes.addFlashAttribute("error", "Không thể thêm vào giỏ hàng!");
                        return "redirect:/productDetail";
                    }
                }
            }else {
                // neu chua co gio hang
                System.out.println(" Chưa có giỏ hàng. Tạo giỏ hàng mới ");
                cartService.createCart(userId);
                Cart cart = cartService.findByUserId(userId);
                if(cartService.addCartItemToCart(cartDTO, cart)){
                    System.out.println("Đã thêm vào giỏ hàng");
                    redirectAttributes.addFlashAttribute("addSuccess", "Đã thêm vào giỏ hàng");
                    return "redirect:/productDetail";
                }else {
                    System.out.println("Không thể thêm vào giỏ hàng");
                    redirectAttributes.addFlashAttribute("error", "Không thể thêm vào giỏ hàng!");
                    return "redirect:/productDetail";
                }
            }
            return "redirect:/productDetail";
//            System.out.println("Đã đăng nhập với id: "+userId);
        }

//        return "redirect:/productDetail";
    }

    @GetMapping("/showCart")
    public String showCart(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findUserByUsername(username);
        Cart cart = cartService.findByUserId(user.getId());

        if(cart == null){
            model.addAttribute("cartEmpty", "Giỏ hàng của bạn trống");
        }else{
            List<CartItem> list = cartService.getListCartItemByCartId(cart.getId());
            if(list.isEmpty()){
                model.addAttribute("cartItemEmpty", "Giỏ hàng của bạn trống");
            }else
                model.addAttribute("list", list);
        }
        return "user/cart";
    }

    @PostMapping("/updateCart")
    public String updateCart(@RequestParam("cartItemId") String idCartItemStr,
                             @RequestParam("newQuantity") String newQuantityStr, Model model){
        System.out.println(idCartItemStr);
        System.out.println(newQuantityStr);
        Long idCartItem = -1L;
        int newQuantity = -1;
        try {
            idCartItem = Long.parseLong(idCartItemStr);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        try {
            newQuantity = Integer.parseInt(newQuantityStr);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        CartItem cartItem = cartService.getCartItemById(idCartItem);
        cartService.updateCartItem(idCartItem, newQuantity);
        return "redirect:/cart/showCart";
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        User user = userService.findUserByUsername(username);
//        Cart cart = cartService.findByUserId(user.getId());
//        if(cart == null){
//            model.addAttribute("cartEmpty", "Giỏ hàng của bạn trống");
//        }else{
//            List<CartItem> list = cartService.getListCartItemByCartId(cart.getId());
//            model.addAttribute("list", list);
//        }
//        return "user/cart";
    }

    @GetMapping("/deleteCartItem/{id}")
    public String deleteCartItem(@PathVariable("id") String idStr){
        Long idCartItem = -1L;
        try {
            idCartItem = Long.parseLong(idStr);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        cartService.deleteCartItemById(idCartItem);
        return "redirect:/cart/showCart";
    }

    @PostMapping("/buyNow")
    public String buyNow(@RequestParam("idProductCart") String idStr, RedirectAttributes redirectAttributes, Model model) {
        Long idProduct = -1L;
        try {
            idProduct = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        redirectAttributes.addAttribute("id", idProduct);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findUserByUsername(username);
        if (user == null) {
            // neu chua dang nhap
            System.out.println("Bạn cần đăng nhập để thêm vào giỏ hàng ");
            Product product = productService.findById(idProduct);
            CategoryDto categoryDto = categoryService.findById(product.getCategory().getId());
            List<Comments> comments = commentService.getCommentsConfirmed(idProduct);

            model.addAttribute("product", product);
            model.addAttribute("category", categoryDto);
            model.addAttribute("listComment", comments);
            model.addAttribute("loginToBuy", "Bạn cần đăng nhập để mua hàng");
            return "user/productDetail";
        } else {
            // da dang nhap
            Long userId = user.getId();

            CartDTO cartDTO = new CartDTO();
            cartDTO.setQuantity(1);
            cartDTO.setProduct(productService.findById(idProduct));
            cartDTO.setTotalPrice(1 * (productService.findById(idProduct).getPrice()));

            // kiem tra user co cart hay chua
            if (cartService.checkCartOfUser(userId)) {
                // neu da co gio hang
                Cart cart = cartService.getCartOfUserByUserId(userId);

                System.out.println(" Đã có giỏ hàng ");
                if (cartService.checkCartItemExists(idProduct, cart.getId())) {
//                    System.out.println("Tăng lên 1");
                    // lay cartItem da tồn tại
                    CartItem cartItem = cartService.getCartItemByProductId(idProduct, cart.getId());
                    if (cartService.updateCartItem(cartItem, 1, cart)) {
                        System.out.println("Update thành công");
//                        redirectAttributes.addFlashAttribute("addSuccess", "Đã thêm vào giỏ hàng");
                        return "redirect:/cart/showCart";
                    }
                } else {
                    System.out.println("Thêm mới cartItem ");
                    Cart cartNew = cartService.findByUserId(userId);
                    if (cartService.addCartItemToCart(cartDTO, cartNew)) {
                        System.out.println("Đã thêm vào giỏ hàng");
//                        redirectAttributes.addFlashAttribute("addSuccess", "Đã thêm vào giỏ hàng");
                        return "redirect:/cart/showCart";
                    } else {
                        System.out.println("Không thể thêm vào giỏ hàng");
                        redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra!");
                        return "redirect:/productDetail";
                    }
                }
            } else {
                // neu chua co gio hang
                System.out.println(" Chưa có giỏ hàng. Tạo giỏ hàng mới ");
                cartService.createCart(userId);
                Cart cart = cartService.findByUserId(userId);
                if (cartService.addCartItemToCart(cartDTO, cart)) {
                    System.out.println("Đã thêm vào giỏ hàng");
//                    redirectAttributes.addFlashAttribute("addSuccess", "Đã thêm vào giỏ hàng");
                    return "redirect:/cart/showCart";
                } else {
                    System.out.println("Không thể thêm vào giỏ hàng");
                    redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra!");
                    return "redirect:/productDetail";
                }
            }
            return "redirect:/cart/showCart";
//            System.out.println("Đã đăng nhập với id: "+userId);
        }
    }
}
