package edu.utycc.reg.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_admin_setting")
public class PaymentAdminSetting {
    
    @Id
    private Long id = 1L; // fixed single row

    @Column(name = "payment_phone_number")
    private String paymentPhoneNumber;

    @Column(name = "payment_qr_path")
    private String paymentQrPath;

    @Column(name = "enrollment_fee")
    private Double enrollmentFee;

    @Column(name = "dormitory_fee")
    private Double dormitoryFee;

    @Column(name = "payment_name")
    private String paymentName;

    public Long getId() {
        return id;
    }

    public PaymentAdminSetting setId(Long id) {
        this.id = id;
        return this;
    }

    public String getPaymentPhoneNumber() {
        return paymentPhoneNumber;
    }

    public PaymentAdminSetting setPaymentPhoneNumber(String paymentPhoneNumber) {
        this.paymentPhoneNumber = paymentPhoneNumber;
        return this;
    }

    public String getPaymentQrPath() {
        return paymentQrPath;
    }

    public PaymentAdminSetting setPaymentQrPath(String paymentQrPath) {
        this.paymentQrPath = paymentQrPath;
        return this;
    }

    public Double getEnrollmentFee() {
        return enrollmentFee;
    }

    public PaymentAdminSetting setEnrollmentFee(Double enrollmentFee) {
        this.enrollmentFee = enrollmentFee;
        return this;
    }

    public Double getDormitoryFee() {
        return dormitoryFee;
    }

    public PaymentAdminSetting setDormitoryFee(Double dormitoryFee) {
        this.dormitoryFee = dormitoryFee;
        return this;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public PaymentAdminSetting setPaymentName(String paymentName) {
        this.paymentName = paymentName;
        return this;
    }


}
