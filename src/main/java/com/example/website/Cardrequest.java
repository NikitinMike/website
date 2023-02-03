package com.example.website;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by vanya on 12.07.2016.
 */
@Entity
@Table(name = "cardrequests", schema = "web")
public class Cardrequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "card_tariff")
    private String cardTariff;

    @Column(name = "card_clause")
    private String cardClause;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birthday")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date birthDay;

    @Column(name = "mobile_phone_number")
    private String mobilePhoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "place_of_birth")
    private String placeOfBirth;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "location_zip_code")
    private String locationZipCode;

    @Column(name = "location_province")
    private String locationProvince;

    @Column(name = "location_district")
    private String locationDistrict;

    @Column(name = "location_city")
    private String locationCity;

    @Column(name = "location_street")
    private String locationStreet;

    @Column(name = "location_house")
    private String locationHouse;

    @Column(name = "location_housing")
    private String locationHousing;

    @Column(name = "location_appartment")
    private String locationAppartment;

    @Column(name = "location_phone_number")
    private String locationPhoneNumber;

    @Column(name = "postal_address")
    private boolean postalAddress;

    @Column(name = "postal_zip_code")
    private String postalZipCode;

    @Column(name = "postal_province")
    private String postalProvince;

    @Column(name = "postal_district")
    private String postalDistrict;

    @Column(name = "postal_city")
    private String postalCity;

    @Column(name = "postal_street")
    private String postalStreet;

    @Column(name = "postal_house")
    private String postalHouse;

    @Column(name = "postal_housing")
    private String postalHousing;

    @Column(name = "postal_apartment")
    private String postalApartment;

    @Column(name = "postal_phone_number")
    private String postalPhoneNumber;

    @Column(name = "pasport_series")
    private String pasportSeries;

    @Column(name = "pasport_number")
    private String pasportNumber;

    @Column(name = "pasport_issued")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date pasportIssued;

    @Column(name = "pasport_code_devision")
    private String pasportCodeDevision;

    @Column(name = "pasport_issued_by")
    private String pasportIssuedBy;

    @Column(name = "place_of_work")
    private String placeOfWork;

    @Column(name = "work_position")
    private String workPosition;

    @Column(name = "work_phone")
    private String workPhone;

    @Column(name = "document_series")
    private String documentSeries;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "document_issued")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date documentIssued;

    @Column(name = "document_code_devision")
    private String documentCodeDevision;

    @Column(name = "document_issued_by")
    private String documentIssuedBy;

    @Column(name = "control_information")
    private String controlInformation;

    @Column(name = "agree_with_personal_information")
    private boolean agreeWithPersonalInformation;

    @Column(name = "agreement")
    private boolean agreement;

    @Column(name = "subscribe")
    private String subscribe;

    @Column(name = "agreement_with_service")
    private boolean agreementWithService;

    @Column(name = "agreement_with_rules")
    private boolean agreementWithRules;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardTariff() {
        return cardTariff;
    }

    public void setCardTariff(String cardTariff) {
        this.cardTariff = cardTariff;
    }

    public String getCardClause() {
        return cardClause;
    }

    public void setCardClause(String cardClause) {
        this.cardClause = cardClause;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getLocationZipCode() {
        return locationZipCode;
    }

    public void setLocationZipCode(String locationZipCode) {
        this.locationZipCode = locationZipCode;
    }

    public String getLocationProvince() {
        return locationProvince;
    }

    public void setLocationProvince(String locationProvince) {
        this.locationProvince = locationProvince;
    }

    public String getLocationDistrict() {
        return locationDistrict;
    }

    public void setLocationDistrict(String locationDistrict) {
        this.locationDistrict = locationDistrict;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getLocationStreet() {
        return locationStreet;
    }

    public void setLocationStreet(String locationStreet) {
        this.locationStreet = locationStreet;
    }

    public String getLocationHouse() {
        return locationHouse;
    }

    public void setLocationHouse(String locationHouse) {
        this.locationHouse = locationHouse;
    }

    public String getLocationHousing() {
        return locationHousing;
    }

    public void setLocationHousing(String locationHousing) {
        this.locationHousing = locationHousing;
    }

    public String getLocationAppartment() {
        return locationAppartment;
    }

    public void setLocationAppartment(String locationAppartment) {
        this.locationAppartment = locationAppartment;
    }

    public String getLocationPhoneNumber() {
        return locationPhoneNumber;
    }

    public void setLocationPhoneNumber(String locationPhoneNumber) {
        this.locationPhoneNumber = locationPhoneNumber;
    }

    public boolean isPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(boolean postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getPostalZipCode() {
        return postalZipCode;
    }

    public void setPostalZipCode(String postalZipCode) {
        this.postalZipCode = postalZipCode;
    }

    public String getPostalProvince() {
        return postalProvince;
    }

    public void setPostalProvince(String postalProvince) {
        this.postalProvince = postalProvince;
    }

    public String getPostalDistrict() {
        return postalDistrict;
    }

    public void setPostalDistrict(String postalDistrict) {
        this.postalDistrict = postalDistrict;
    }

    public String getPostalCity() {
        return postalCity;
    }

    public void setPostalCity(String postalCity) {
        this.postalCity = postalCity;
    }

    public String getPostalStreet() {
        return postalStreet;
    }

    public void setPostalStreet(String postalStreet) {
        this.postalStreet = postalStreet;
    }

    public String getPostalHouse() {
        return postalHouse;
    }

    public void setPostalHouse(String postalHouse) {
        this.postalHouse = postalHouse;
    }

    public String getPostalHousing() {
        return postalHousing;
    }

    public void setPostalHousing(String postalHousing) {
        this.postalHousing = postalHousing;
    }

    public String getPostalApartment() {
        return postalApartment;
    }

    public void setPostalApartment(String postalApartment) {
        this.postalApartment = postalApartment;
    }

    public String getPostalPhoneNumber() {
        return postalPhoneNumber;
    }

    public void setPostalPhoneNumber(String postalPhoneNumber) {
        this.postalPhoneNumber = postalPhoneNumber;
    }

    public String getPasportSeries() {
        return pasportSeries;
    }

    public void setPasportSeries(String pasportSeries) {
        this.pasportSeries = pasportSeries;
    }

    public String getPasportNumber() {
        return pasportNumber;
    }

    public void setPasportNumber(String pasportNumber) {
        this.pasportNumber = pasportNumber;
    }

    public Date getPasportIssued() {
        return pasportIssued;
    }

    public void setPasportIssued(Date pasportIssued) {
        this.pasportIssued = pasportIssued;
    }

    public String getPasportCodeDevision() {
        return pasportCodeDevision;
    }

    public void setPasportCodeDevision(String pasportCodeDevision) {
        this.pasportCodeDevision = pasportCodeDevision;
    }

    public String getPasportIssuedBy() {
        return pasportIssuedBy;
    }

    public void setPasportIssuedBy(String pasportIssuedBy) {
        this.pasportIssuedBy = pasportIssuedBy;
    }


    public String getPlaceOfWork() {
        return placeOfWork;
    }

    public void setPlaceOfWork(String placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    public String getWorkPosition() {
        return workPosition;
    }

    public void setWorkPosition(String workPosition) {
        this.workPosition = workPosition;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getDocumentSeries() {
        return documentSeries;
    }

    public void setDocumentSeries(String documentSeries) {
        this.documentSeries = documentSeries;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Date getDocumentIssued() {
        return documentIssued;
    }

    public void setDocumentIssued(Date documentIssued) {
        this.documentIssued = documentIssued;
    }

    public String getDocumentCodeDevision() {
        return documentCodeDevision;
    }

    public void setDocumentCodeDevision(String documentCodeDevision) {
        this.documentCodeDevision = documentCodeDevision;
    }

    public String getDocumentIssuedBy() {
        return documentIssuedBy;
    }

    public void setDocumentIssuedBy(String documentIssuedBy) {
        this.documentIssuedBy = documentIssuedBy;
    }

    public String getControlInformation() {
        return controlInformation;
    }

    public void setControlInformation(String controlInformation) {
        this.controlInformation = controlInformation;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public boolean isAgreeWithPersonalInformation() {
        return agreeWithPersonalInformation;
    }

    public void setAgreeWithPersonalInformation(boolean agreeWithPersonalInformation) {
        this.agreeWithPersonalInformation = agreeWithPersonalInformation;
    }

    public boolean isAgreement() {
        return agreement;
    }

    public void setAgreement(boolean agreement) {
        this.agreement = agreement;
    }

    public boolean isAgreementWithService() {
        return agreementWithService;
    }

    public void setAgreementWithService(boolean agreementWithService) {
        this.agreementWithService = agreementWithService;
    }

    public boolean isAgreementWithRules() {
        return agreementWithRules;
    }

    public void setAgreementWithRules(boolean agreementWithRules) {
        this.agreementWithRules = agreementWithRules;
    }
}
