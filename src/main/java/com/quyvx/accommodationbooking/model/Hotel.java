package com.quyvx.accommodationbooking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.util.List;

@Getter
@Setter
@Entity
@ToString
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "location", columnDefinition = "TEXT")
    private String location;

    private float score = 0;

    @Column(name = "short_description", columnDefinition = "TEXT")
    private String shortDescription;

    @Column(name = "detail_description", columnDefinition = "TEXT")
    private String detailDescription;

    private int assess;

    @Column(name = "avatar_hotel", columnDefinition = "TEXT")
    private String avatarHotel;

    @Column(name = "number_rating")
    private int numberRating = 0;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;


}
