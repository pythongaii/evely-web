$(function(){
    $('.event-card').click(function() {
        var eventId = $(this).attr('event-id');
        window.location = '/event/edit/' + eventId;
    });
})