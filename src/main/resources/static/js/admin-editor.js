$(document).ready(function() {
    console.log("admin-editor.js loaded. jQuery available:", typeof jQuery !== 'undefined');

    const $description = $('#description');
    const $index = $('#index');

    if ($description.length) {
        console.log("Initializing Summernote for #description.");
        $description.summernote({
            height: 200,
            minHeight: null,
            maxHeight: null,
            focus: false
        });
    } else {
        console.log("#description element not found.");
    }

    if ($index.length) {
        console.log("Initializing Summernote for #index.");
        $index.summernote({
            height: 150,
            minHeight: null,
            maxHeight: null,
            focus: false
        });
    } else {
        console.log("#index element not found.");
    }

    $('form').on('submit', function() {
        console.log("Form submitted. Syncing Summernote content.");

        if ($description.length) {
            $description.val($description.summernote('code'));
        }
        if ($index.length) {
            $index.val($index.summernote('code'));
        }
    });
});
