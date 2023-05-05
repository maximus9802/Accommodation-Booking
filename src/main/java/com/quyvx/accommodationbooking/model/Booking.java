package com.quyvx.accommodationbooking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="create_date", nullable = false, updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    @Column(name = "update_date")
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;

    @Column(name ="date_check_in")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Please choose date check in")
    private Date dateCheckIn;

    @Column(name ="date_check_out")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Please choose date check out")
    private Date dateCheckOut;

    @Column(name = "total_bill")
    private int totalBill;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Rating> ratings;

    @ManyToMany(mappedBy = "bookings")
    private Set<Room> rooms = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
