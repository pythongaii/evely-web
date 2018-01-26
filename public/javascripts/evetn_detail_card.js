$(function () {

    /* category tab */
    const scrollerEl = document.querySelector('#my-mdc-tab-bar-scroller');
    const tabBarScroller = new mdc.tabs.MDCTabBarScroller(scrollerEl);
    /* tab switching */
    var dynamicTabBar = window.dynamicTabBar = new mdc.tabs.MDCTabBar(document.querySelector('#my-scrollable-tab-bar'));
    var panels = document.querySelector('.panels');
    var dots = document.querySelector('.dots');

    var pin = document.querySelector('#event-pin');

    pin.addEventListener('click', function(){
        var image = this.style.backgroundImage;
        if(image == 'url("/assets/images/icon/ic_pin/ic_pin_tinted.png")'){
            this.style.backgroundImage = 'url("/assets/images/icon/ic_pin/ic_pin.png")';
        } else {
            this.style.backgroundImage = 'url("/assets/images/icon/ic_pin/ic_pin_tinted.png")';
        }
    });

    function updateDot(index) {
        var activeDot = dots.querySelector('.dot.active');
        if (activeDot) {
            activeDot.classList.remove('active');
        }
        var newActiveDot = dots.querySelector('.dot:nth-child(' + (index + 1) + ')');
        if (newActiveDot) {
            newActiveDot.classList.add('active');
        }
    }

    function updatePanel(index) {
        var activePanel = panels.querySelector('.panel.active');
        if (activePanel) {
            activePanel.classList.remove('active');
        }
        var newActivePanel = panels.querySelector('.panel:nth-child(' + (index + 1) + ')');
        if (newActivePanel) {
            newActivePanel.classList.add('active');
        }
        if (newActivePanel.id == "panel-1") {
            navigator.geolocation.getCurrentPosition(
                function (position) {
                    var mapLatLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                    var okayamap = new google.maps.Map(document.getElementById("map"), {
                        center: mapLatLng,
                        zoom: 18,
                        mapTypeId: google.maps.MapTypeId.ROADMAP
                    });
                    // ドラッグできるマーカーを表示
                    var marker = new google.maps.Marker({
                        position: mapLatLng,
                        title: "Okayama the Legend!",
                        draggable: true	// ドラッグ可能にする
                    });
                    marker.setMap(okayamap);

                    var gc = new google.maps.Geocoder();

                    // マーカーのドロップ（ドラッグ終了）時のイベント
                    google.maps.event.addListener(marker, 'dragend', function (ev) {
                        // var latlng = new google.maps.LatLng(ev.latLng.lat(), ev.latlng.lng());
                        gc.geocode({latLng: this.position},

                            function (results, status) {
                                if (status == google.maps.GeocoderStatus.OK) {
                                    var adrsData = results[0].address_components;
                                    var txt = "";
                                    var postalField = document.getElementById("postal-code-text-field");
                                    postalField.value = adrsData[adrsData.length - 1].long_name;
                                    var evt = document.createEvent("HTMLEvents");
                                    evt.initEvent('focus', true, true ); // event type, bubbling, cancelable
                                    postalField.dispatchEvent(evt);
                                    for (var i = adrsData.length - 3; i > 0; i--) {
                                        txt += adrsData[i].long_name;
                                    }
                                    var addrsField = document.getElementById("address-text-field");
                                    addrsField.value = txt;
                                    addrsField.dispatchEvent(evt);
                                } else {
                                    alert(status + " : ジオコードに失敗しました");
                                }
                            });

                        // document.getElementById('latitude').value = ev.latLng.lat();
                        // document.getElementById('longitude').value = ev.latLng.lng();
                    });

                }
                ,
                // 取得失敗した場合
                function (error) {
                    // エラーメッセージを表示
                    switch (error.code) {
                        case 1: // PERMISSION_DENIED
                            alert("位置情報の利用が許可されていません");
                            break;
                        case 2: // POSITION_UNAVAILABLE
                            alert("現在位置が取得できませんでした");
                            break;
                        case 3: // TIMEOUT
                            alert("タイムアウトになりました");
                            break;
                        default:
                            alert("その他のエラー(エラーコード:" + error.code + ")");
                            break;
                    }
                }
            );


        }
    }

    navigator.geolocation.getCurrentPosition(
        function (position) {
            var mapLatLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
            var okayamap = new google.maps.Map(document.getElementById("map"), {
                center: mapLatLng,
                zoom: 18,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            });
            // ドラッグできるマーカーを表示
            var marker = new google.maps.Marker({
                position: mapLatLng,
                title: "Okayama the Legend!",
                draggable: true	// ドラッグ可能にする
            });
            marker.setMap(okayamap);

            var gc = new google.maps.Geocoder();

            // マーカーのドロップ（ドラッグ終了）時のイベント
            google.maps.event.addListener(marker, 'dragend', function (ev) {
                // var latlng = new google.maps.LatLng(ev.latLng.lat(), ev.latlng.lng());
                gc.geocode({latLng: this.position},

                    function (results, status) {
                        if (status == google.maps.GeocoderStatus.OK) {
                            var adrsData = results[0].address_components;
                            var txt = "";
                            var postalField = document.getElementById("postal-code-text-field");
                            postalField.value = adrsData[adrsData.length - 1].long_name;
                            var evt = document.createEvent("HTMLEvents");
                            evt.initEvent('focus', true, true ); // event type, bubbling, cancelable
                            postalField.dispatchEvent(evt);
                            for (var i = adrsData.length - 3; i > 0; i--) {
                                txt += adrsData[i].long_name;
                            }
                            var addrsField = document.getElementById("address-text-field");
                            addrsField.value = txt;
                            addrsField.dispatchEvent(evt);
                        } else {
                            alert(status + " : ジオコードに失敗しました");
                        }
                    });

                // document.getElementById('latitude').value = ev.latLng.lat();
                // document.getElementById('longitude').value = ev.latLng.lng();
            });

        }
        ,
        // 取得失敗した場合
        function (error) {
            // エラーメッセージを表示
            switch (error.code) {
                case 1: // PERMISSION_DENIED
                    alert("位置情報の利用が許可されていません");
                    break;
                case 2: // POSITION_UNAVAILABLE
                    alert("現在位置が取得できませんでした");
                    break;
                case 3: // TIMEOUT
                    alert("タイムアウトになりました");
                    break;
                default:
                    alert("その他のエラー(エラーコード:" + error.code + ")");
                    break;
            }
        }
    );


    dynamicTabBar.listen('MDCTabBar:change', function ({detail: tabs}) {
        var nthChildIndex = tabs.activeTabIndex;

        updatePanel(nthChildIndex);
        updateDot(nthChildIndex);
    });

    dots.addEventListener('click', function (evt) {
        if (!evt.target.classList.contains('dot')) {
            return;
        }

        evt.preventDefault();

        var dotIndex = [].slice.call(dots.querySelectorAll('.dot')).indexOf(evt.target);

        if (dotIndex >= 0) {
            dynamicTabBar.activeTabIndex = dotIndex;
        }

        updatePanel(dotIndex);
        updateDot(dotIndex);

    });

    function initMap() {
        navigator.geolocation.getCurrentPosition(
            function (position) {
                var mapLatLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                // // マップオプションを変数に格納
                // var mapOptions = {
                //     zoom: 15,          // 拡大倍率
                //     center: mapLatLng  // 緯度・経度
                // };
                // // マップオブジェクト作成
                // var map = new google.maps.Map(
                //     document.getElementById("map"), // マップを表示する要素
                //     mapOptions         // マップオプション
                // );
                // マップ表示
                var okayamap = new google.maps.Map(document.getElementById("map"), {
                    center: mapLatLng,
                    zoom: 13,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                });
                // ドラッグできるマーカーを表示
                var marker = new google.maps.Marker({
                    position: mapLatLng,
                    title: "Okayama the Legend!",
                    draggable: true	// ドラッグ可能にする
                });
                marker.setMap(okayamap);

                var gc = new google.maps.Geocoder();

                // マーカーのドロップ（ドラッグ終了）時のイベント
                google.maps.event.addListener(marker, 'dragend', function (ev) {
                    // イベントの引数evの、プロパティ.latLngが緯度経度。
                    alert(ev);
                    // var latlng = new google.maps.LatLng(ev.latLng.lat(), ev.latlng.lng());
                    gc.geocode({latLng: this.position},

                        function (results, status) {
                            if (status == google.maps.GeocoderStatus.OK) {
                                var adrsData = results[0].address_components;
                                var txt = "";
                                for (var i = 0; i < adrsData.length; i++) {
                                    txt += adrsData[i].long_name + " , ";
                                }
                                document.getElementById("address-text-field").value = txt;
                            } else {
                                alert(status + " : ジオコードに失敗しました");
                            }
                        });

                    // document.getElementById('latitude').value = ev.latLng.lat();
                    // document.getElementById('longitude').value = ev.latLng.lng();
                });

            }
            ,
            // 取得失敗した場合
            function (error) {
                // エラーメッセージを表示
                switch (error.code) {
                    case 1: // PERMISSION_DENIED
                        alert("位置情報の利用が許可されていません");
                        break;
                    case 2: // POSITION_UNAVAILABLE
                        alert("現在位置が取得できませんでした");
                        break;
                    case 3: // TIMEOUT
                        alert("タイムアウトになりました");
                        break;
                    default:
                        alert("その他のエラー(エラーコード:" + error.code + ")");
                        break;
                }
            }
        );


    }
});