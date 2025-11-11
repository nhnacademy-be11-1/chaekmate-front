document.addEventListener('DOMContentLoaded', function() {
    const selector = document.querySelector('.tag-selector');
    if (!selector) {
        return;
    }

    const container = selector;
    const idsInput = document.getElementById('tagIds');
    const namesSpan = document.getElementById('selected-tag-names');

    const initialIds = container.dataset.initialIds || '';
    const initialNames = container.dataset.initialNames || '';

    let selectedTags = new Map();

    // Initialize with existing selections
    if (initialIds && initialNames) {
        const ids = initialIds.split(',').filter(id => id.trim() !== '');
        const names = initialNames.split(',').filter(name => name.trim() !== '');

        if (ids.length === names.length) {
            for (let i = 0; i < ids.length; i++) {
                selectedTags.set(ids[i], names[i]);
            }
        }
    }

    // Pre-highlight selected items in the tree
    const allTagItems = container.querySelectorAll('.tree-item'); // Using generic .tree-item from fragment
    for (const item of allTagItems) {
        const id = item.dataset.id;
        if (selectedTags.has(id)) {
            item.classList.add('font-weight-bold', 'text-primary');
        }
    }

    container.addEventListener('click', function(e) {
        // Item selection
        if (e.target.classList.contains('tree-item')) {
            const id = e.target.dataset.id;
            const name = e.target.textContent;

            if (selectedTags.has(id)) {
                selectedTags.delete(id);
                e.target.classList.remove('font-weight-bold', 'text-primary');
            } else {
                selectedTags.set(id, name);
                e.target.classList.add('font-weight-bold', 'text-primary');
            }

            // Update display and hidden input
            const updatedNames = Array.from(selectedTags.values());
            const updatedIds = Array.from(selectedTags.keys());
            namesSpan.textContent = updatedNames.length > 0 ? updatedNames.join(', ') : '없음';
            idsInput.value = updatedIds.join(',');
        }
    });
});