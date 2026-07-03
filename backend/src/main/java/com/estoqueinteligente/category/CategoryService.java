package com.estoqueinteligente.category;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.estoqueinteligente.common.BusinessException;
import com.estoqueinteligente.common.ResourceNotFoundException;

@Service
public class CategoryService {
    private final CategoryRepository repository;
    public CategoryService(CategoryRepository repository) { this.repository = repository; }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return repository.findAll(Sort.by("name")).stream().map(CategoryResponse::from).toList();
    }
    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) { return CategoryResponse.from(getEntity(id)); }
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        if (repository.existsByNameIgnoreCase(request.name().trim())) throw new BusinessException("Já existe uma categoria com este nome");
        Category category = new Category();
        apply(category, request);
        return CategoryResponse.from(repository.save(category));
    }
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = getEntity(id);
        if (!category.getName().equalsIgnoreCase(request.name().trim()) && repository.existsByNameIgnoreCase(request.name().trim()))
            throw new BusinessException("Já existe uma categoria com este nome");
        apply(category, request);
        return CategoryResponse.from(repository.save(category));
    }
    @Transactional public void delete(Long id) { repository.delete(getEntity(id)); repository.flush(); }
    @Transactional(readOnly = true)
    public Category getEntity(Long id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada")); }
    private void apply(Category category, CategoryRequest request) {
        category.setName(request.name().trim());
        category.setDescription(request.description() == null ? null : request.description().trim());
    }
}
