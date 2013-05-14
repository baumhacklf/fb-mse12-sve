package sve2.fhbay.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({
		@NamedQuery(name = "qryFindByName", query = "select c from Category c where lower(c.name) like :pattern"),
		@NamedQuery(name = "qryFindRootCategories", query = "select c from Category c where parentcategory_id IS NULL") })
public class Category implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	private String name;

	@ManyToOne(cascade = CascadeType.ALL, optional = true)
	private Category parentCategory;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Category> subCategories = new HashSet<Category>();

	public Category() {

	}

	public Category(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getParentCategoryId() {
		return parentCategory;
	}

	public void setParentCategoryId(Category parentCategoryId) {
		this.parentCategory = parentCategoryId;
	}

	public Set<Category> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(Set<Category> subCategories) {
		this.subCategories = subCategories;
	}

	/*
	 * Comfort Method
	 */
	public void addSubCategory(Category category) {
		subCategories.add(category);
		category.setParentCategoryId(this);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: " + name);
		if (subCategories.size() > 0)
			sb.append(" Number of SubCategories: " + subCategories.size());
		if (parentCategory != null)
			sb.append(" Parent Category: " + parentCategory.getName());
		return sb.toString();
	}

}
