    function deleteSelectedImages() {
    const checkboxes = document.querySelectorAll('.delete-checkbox');
    checkboxes.forEach(checkbox => {
    if (checkbox.checked) {
    const container = checkbox.closest('.image-container');
    if (container) {
    container.remove();
}
}
});
}

