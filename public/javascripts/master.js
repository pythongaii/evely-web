

$(function() {
    $('.search form input').focusin(function() {
        $(this).css({
            'border': '1px solid #8e8e8e'
        });
    }).focusout(function() {
        if ($('#night-mode').hasClass('on')) {
            $(this).css({
                'border': '1px solid #4a4a4a'
            });
        } else {
            $(this).css({
                'border': '1px solid #e6ecf0'
            });
        }
    });
    // ブックマーク
    $('.action-bookmark').click(function() {
        $(this).toggleClass('on');
    });

    $(".bookmark").on('click', function(){
        $(this).toggleClass('on');
    });

    $('#header-title').on('click',function () {
       window.location.href="http://localhost:8888/index";
    });

    $('input').on('blur', function(e) {
        e.target.parentElement.classList.add("focused");
    });

});