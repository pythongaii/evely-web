$(function() {

    /* profile menu */
    let profileMenu = new mdc.menu.MDCSimpleMenu(document.querySelector('#profileMenu'));
    document.querySelector('#openMenu').addEventListener('click', () => profileMenu.open = !profileMenu.open);

    /* event menu */
    let eventMenu = new mdc.menu.MDCSimpleMenu(document.querySelector('#eventMenu'));
    document.querySelector('#event-menu-open').addEventListener('click', () => eventMenu.open = !eventMenu.open);
});
