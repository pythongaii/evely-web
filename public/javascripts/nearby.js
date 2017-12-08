$(function() {
    var map; // Googleマップインスタンス
    var geo; // 地名-緯度経度のためのgeocodeインスタンス
    // Google Mapで利用する初期設定用の変数
    var latlng = new google.maps.LatLng(34.7065867, 135.5021337);
    var opts = {
        zoom: 15,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        center: latlng,
        disableDefaultUI: true
    };
    // id="map"を示すタグからget(0)メソッドで最初の要素を取出しmapにセット
    map = new google.maps.Map($("#map").get(0), opts);
    // ジオコードリクエストを送信するGeocoderの作成
    geo = new google.maps.Geocoder();

    // ジオコードリクエストが成功したときに呼び出されるコールバック関数
    var geoResultCallback = function (result, status) {
        if (status != google.maps.GeocoderStatus.OK) {
            alert(status);
            return;
        }
        latlng = result[0].geometry.location;
        marker = new google.maps.Marker({position:latlng, map:map}); // マーカをセット
        map.setCenter(marker.position); // マーカー位置を中心にセット
    }

    // [三]をクリックでイベント詳細が左から出てくる
    $('.search-box-btn.menu').click(function() {
        $('.side-nav').toggleClass('visible');
        if ($('.side-nav').hasClass('visible')) {
            $('.search-box').css('opacity', 0.85);
        } else {
            $('.search-box').css('opacity', 1);
        }
    });

    $('.search-box-btn').hover(
        function() {
            if ($('#night-mode').hasClass('on')) {
                $(this).css({
                    'color': 'white'
                });
            } else {
                $(this).css({
                    'color': 'black'
                });
            }
        },
        function() {
            $(this).css({
                'color': 'gray'
            });
        }
    );

    $('.event-images').hover(
        function() {
            $('.event-other-image').css({
                'color': '#fffe',
                'height': '75%',
                'line-height': '18'
            });
        },
        function() {
            $('.event-other-image').css({
                'color': '#fffc',
                'height': '50%',
                'line-height': '12'
            });
        }
    );
});
