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

    $('#night-mode').click(function() {
        $(this).toggleClass('on');
        if ($(this).hasClass('on')) {
            $('.app-bar').css({
                'background-color': '#1c1c1c'
            });
            $('.search form input').css({
                'background-color': '#2d2d2d',
                'border': '1px solid #4a4a4a',
                'color': '#dedede'
            });
            $('.search-box').css({
                'background': '#1c1c1c'
            });
            $('.search-box input').css({
                'color': '#dedede'
            });
            $('.side-nav').css({
                'background': '#1c1c1c',
                'color': '#dedede'
            });
            $('.side-nav-separator1').css({
                'border-top': '1px solid #535353',
                'border-bottom': '1px solid #535353',
                'background-image': 'linear-gradient(-45deg, #000 25%, #535353 25%, #535353 50%, #000 50%, #000 75%, #535353 75%, #535353 100%)'
            });
            $('.side-nav-separator2').css({
                'border-top': '1px solid #dedede'
            });
        } else {
            $('.app-bar').css({
                'background-color': '#fff'
            });
            $('.search form input').css({
                'background-color': '#f5f8fa',
                'border': '1px solid #e6ecf0',
                'color': '#14171a'
            });
            $('.search-box').css({
                'background': '#fff'
            });
            $('.search-box input').css({
                'color': ''
            });
            $('.side-nav').css({
                'background': '#fff',
                'color': ''
            });
            $('.side-nav-separator1').css({
                'border-top': '1px solid #acacac',
                'border-bottom': '1px solid #acacac',
                'background-image': 'linear-gradient(-45deg, #fff 25%, #acacac 25%, #acacac 50%, #fff 50%, #fff 75%, #acacac 75%, #acacac 100%)'
            });
            $('.side-nav-separator2').css({
                'border-top': '1px solid #8e8e8e'
            });
        }
    });

    // ピン
    $('.action-pin').click(function() {
        $(this).toggleClass('on');
    });

    $(".pin").on('click', function(){
        $(this).toggleClass('on');
    });
});
