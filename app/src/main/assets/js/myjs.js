DecoupledEditor
        .create(document.querySelector('#editor'), {
            toolbar: {
                items: ['bold', 'italic', 'bulletedList', 'numberedList', 'blockQuote', 'imageUpload']
            }
        })
        .then(editor => {
            window.editor = editor;
            const toolbarContainer = document.querySelector('#toolbar-container');
            toolbarContainer.appendChild(editor.ui.view.toolbar.element);
        })
        .catch(error => {
            console.error(error);
        });