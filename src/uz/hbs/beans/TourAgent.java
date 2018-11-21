package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class TourAgent implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final long INTERNAL = -3;

	private Long users_id;
	private String name;
	private String city;
	private String primary_phone;
	private String secondary_phone;
	private String email;
	private String display_name;
	private Byte status;
	private byte[] license;
	private byte[] cert;
	private String license_id;
	private String cert_id;
	private String licenseFileInput;
	private String certFileInput;
	private Long addresses_id;
	private long creator_user_id;
	private Date create_date;
	private String tourAgentName;
	
	private Address addresses = new Address();
	private Accountant accountants = new Accountant();
	private Bank banks_primary = new Bank();
	private Bank banks_secondary = new Bank();
	private Manager managers = new Manager();
	private LicenseAndCert licensesAndCerts = new LicenseAndCert();
	private Contract contract = new Contract();
	private boolean corp;
	private boolean workable;

	public TourAgent() {
	}
	
	public TourAgent(long users_id, String name) {
		this.users_id = users_id;
		this.name = name;
	}

	public TourAgent(long users_id) {
		this.users_id = users_id;
	}
	
	@Override
	public String toString() {
		return "{users_id=" + users_id + ", addresses_id=" + addresses_id + ", display_name=" + display_name + ", primary_phone=" + primary_phone
				+ ", secondary_phone=" + secondary_phone + "}";
	}

	public Long getUsers_id() {
		return users_id;
	}

	public void setUsers_id(Long users_id) {
		this.users_id = users_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPrimary_phone() {
		return primary_phone;
	}

	public void setPrimary_phone(String primary_phone) {
		this.primary_phone = primary_phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getAddresses() {
		return addresses;
	}

	public void setAddresses(Address addresses) {
		this.addresses = addresses;
	}

	public Accountant getAccountants() {
		return accountants;
	}

	public void setAccountants(Accountant accountants) {
		this.accountants = accountants;
	}

	public Bank getBanks_primary() {
		return banks_primary;
	}

	public void setBanks_primary(Bank banks_primary) {
		this.banks_primary = banks_primary;
	}

	public Bank getBanks_secondary() {
		return banks_secondary;
	}

	public void setBanks_secondary(Bank banks_secondary) {
		this.banks_secondary = banks_secondary;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Manager getManagers() {
		return managers;
	}

	public void setManagers(Manager managers) {
		this.managers = managers;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public String getSecondary_phone() {
		return secondary_phone;
	}

	public void setSecondary_phone(String secondary_phone) {
		this.secondary_phone = secondary_phone;
	}

	public String getLicense_id() {
		return license_id;
	}

	public void setLicense_id(String license_id) {
		this.license_id = license_id;
	}

	public String getCert_id() {
		return cert_id;
	}

	public void setCert_id(String cert_id) {
		this.cert_id = cert_id;
	}

	public byte[] getLicense() {
		return license;
	}

	public void setLicense(byte[] license) {
		this.license = license;
	}

	public byte[] getCert() {
		return cert;
	}

	public void setCert(byte[] cert) {
		this.cert = cert;
	}

	public String getLicenseFileInput() {
		return licenseFileInput;
	}

	public void setLicenseFileInput(String licenseFileInput) {
		this.licenseFileInput = licenseFileInput;
	}

	public String getCertFileInput() {
		return certFileInput;
	}

	public void setCertFileInput(String certFileInput) {
		this.certFileInput = certFileInput;
	}

	public Long getAddresses_id() {
		return addresses_id;
	}

	public void setAddresses_id(Long addresses_id) {
		this.addresses_id = addresses_id;
	}

	public LicenseAndCert getLicensesAndCerts() {
		return licensesAndCerts;
	}

	public void setLicensesAndCerts(LicenseAndCert licensesAndCerts) {
		this.licensesAndCerts = licensesAndCerts;
	}

	public long getCreator_user_id() {
		return creator_user_id;
	}

	public void setCreator_user_id(long creator_user_id) {
		this.creator_user_id = creator_user_id;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getTourAgentName() {
		return tourAgentName;
	}

	public void setTourAgentName(String tourAgentName) {
		this.tourAgentName = tourAgentName;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public boolean isCorp() {
		return corp;
	}

	public void setCorp(boolean corp) {
		this.corp = corp;
	}

	public boolean isWorkable() {
		return workable;
	}

	public void setWorkable(boolean workable) {
		this.workable = workable;
	}
}
