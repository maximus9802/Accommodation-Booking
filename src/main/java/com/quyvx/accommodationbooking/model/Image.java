package com.quyvx.accommodationbooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "link_image")
    @NotNull(message = "Please enter the link image")
    private String linkImage;

    @ManyToMany(mappedBy = "images")
    private Set<Room> rooms = new HashSet<>();
}
