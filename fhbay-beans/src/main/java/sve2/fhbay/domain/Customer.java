package sve2.fhbay.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;

@Entity
@NamedQueries({
	@NamedQuery(name = "qryFindCustomerByUsername", query = "select c from Customer c where lower(c.username) like :pattern"),
	@NamedQuery(name = "qryFindCustomerByName", query = "select c from Customer c where lower(c.lastname) like :pattern")
})

public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	// @Transient = not mapped to db
	@Id
	@GeneratedValue
	private Long id;
	@Column(length = 20)
	private String firstname;
	private String lastname;
	private String username;
	private String password;
	@Column(nullable = false)
	private String email;
	
	@Enumerated(EnumType.STRING)
	private CustomerRole role = CustomerRole.USER;

	// wenn keine referenz mehr wird gelöscht (orphanRemoval)
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Address billingAddress;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn
	@OrderColumn(name="INDEX")
	private List<Address> shippingAddresses = new ArrayList<Address>();
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@MapKeyColumn(name = "PHONE_TYPE")
	private Map<String, Phone> phones = new HashMap<String, Phone>();
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@MapKeyColumn(name = "PAYMENT_DATA")
	private List<PaymentData> paymentData = new ArrayList<PaymentData>();

	/*
	 * Constructors
	 */
	public Customer() {
	}

	public Customer(String firstname, String lastname, String username,
			String password, String email) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.password = password;
		this.email = email;
	}

	/*
	 * Getters & Setters
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	public List<Address> getShippingAddresses() {
		return shippingAddresses;
	}

	public void setShippingAddresses(List<Address> shippingAddresses) {
		this.shippingAddresses = shippingAddresses;
	}
	
	public List<PaymentData> getPaymentData() {
		return paymentData;
	}

	public void setPaymentData(List<PaymentData> paymentData) {
		this.paymentData = paymentData;
	}

	public CustomerRole getRole() {
		return role;
	}

	public void setRole(CustomerRole role) {
		this.role = role;
	}

	/*
	 * comfort method
	 */
	public void addShippingAddress(Address address) {
		shippingAddresses.add(address);
	}
	
	public void addPaymentData(PaymentData paymentData) {
		this.paymentData.add(paymentData);
	}
	
	public Map<String, Phone> getPhones() {
		return phones;
	}

	public void setPhones(Map<String, Phone> phones) {
		this.phones = phones;
	}
	
	public void addPhone(String type, Phone phone) {
		phones.put(type, phone);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id: " + id);
		sb.append(" lastname: " + lastname);
		sb.append(" firstname: " + firstname);
		sb.append(" username: " + username);
		sb.append(" email: " + email);
		if (billingAddress != null)
			sb.append(" Billing Address: " + billingAddress);
		return sb.toString();
	}
}
