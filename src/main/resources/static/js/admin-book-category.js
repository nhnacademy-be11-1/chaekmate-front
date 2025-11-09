document.addEventListener('DOMContentLoaded', function() {
    const selector = document.querySelector('.category-selector');
    if (!selector) {
        return;
    }

    const container = selector;
    const idsInput = document.getElementById('categoryIds');
    const namesSpan = document.getElementById('selected-category-names');

    const initialIds = container.dataset.initialIds || '';
    const initialNames = container.dataset.initialNames || '';

    let selectedCategories = new Map();

    // Initialize with existing selections
    if (initialIds && initialNames) {
        const ids = initialIds.split(',').filter(id => id.trim() !== '');
        const names = initialNames.split(',').filter(name => name.trim() !== '');

        if (ids.length === names.length) {
            for (let i = 0; i < ids.length; i++) {
                selectedCategories.set(ids[i], names[i]);
            }
        }
    }

    // Pre-highlight selected items in the tree
    const allCategoryItems = container.querySelectorAll('.category-item');
    for (const item of allCategoryItems) {
        const id = item.dataset.id;
        if (selectedCategories.has(id)) {
            item.classList.add('font-weight-bold', 'text-primary');
        }
    }

    container.addEventListener('click', function(e) {
        // Toggle button
        if (e.target.classList.contains('toggle-btn')) {
            const sublist = e.target.closest('li').querySelector('ul');
            if (sublist) {
                const isHidden = sublist.style.display === 'none' || sublist.style.display === '';
                sublist.style.display = isHidden ? 'block' : 'none';
                e.target.textContent = isHidden ? '-' : '+';
            }
        }

        // Category item selection
        if (e.target.classList.contains('category-item')) {
            const id = e.target.dataset.id;
            const name = e.target.textContent;

            if (selectedCategories.has(id)) {
                selectedCategories.delete(id);
                e.target.classList.remove('font-weight-bold', 'text-primary');
            } else {
                selectedCategories.set(id, name);
                e.target.classList.add('font-weight-bold', 'text-primary');
            }

            // Update display and hidden input
            const updatedNames = Array.from(selectedCategories.values());
            const updatedIds = Array.from(selectedCategories.keys());
            namesSpan.textContent = updatedNames.length > 0 ? updatedNames.join(', ') : '없음';
            idsInput.value = updatedIds.join(',');
        }
    });
});