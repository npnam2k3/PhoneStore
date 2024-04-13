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


function onlyOneCheckboxChecked(checkbox) {
    var checkboxes = document.querySelectorAll('input[type="checkbox"]');
    checkboxes.forEach(function (cb) {
        if (cb !== checkbox) {
            cb.checked = false;
        }
    });
}

// Lắng nghe sự kiện click trên các checkbox
document.addEventListener('DOMContentLoaded', function () {
    var checkboxes = document.querySelectorAll('input[type="checkbox"]');
    checkboxes.forEach(function (checkbox) {
        checkbox.addEventListener('click', function () {
            onlyOneCheckboxChecked(checkbox);
        });
    });
});