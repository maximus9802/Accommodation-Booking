package com.quyvx.accommodationbooking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@ToString
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int price;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "room_type")
    private String roomType;

    private float score;

    private List<String> service = new ArrayList<>();

    private List<String> images = new ArrayList<>();

    private int numberRating;
    public Room(){
        this.score = 0;
        this.numberRating =0;
    }

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "room", cascade =  CascadeType.ALL)
    private List<Rating> ratings;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "booking_room",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "booking_id")
    )
    private Set<Booking> bookings = new HashSet<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<DateRent> dateRents;


}
