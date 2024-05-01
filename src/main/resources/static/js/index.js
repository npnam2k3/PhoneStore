document.addEventListener('DOMContentLoaded', function() {
    window.addEventListener('scroll', function() {
        if (window.scrollY) {
            document.getElementById('back-top').style.opacity = '1'; // Hiển thị nút
        } else {
            document.getElementById('back-top').style.opacity = '0'; // Ẩn nút
        }
    });

    document.getElementById('back-top').addEventListener('click', function() {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    });
});

