package com.app.pfmjee.Entities;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "film")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private Short filmId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column( name = "release_year")
    private Short releaseYear;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "original_language_id")
    private Language originalLanguage;

    @Column( name = "rental_duration")
    private Byte rentalDuration;
    @Column( name = "rental_rate")
    private BigDecimal rentalRate;
    private Short length;

    @Column( name = "replacement_cost")
    private BigDecimal replacementCost;

    @Column(name = "rating", nullable = true)
    private String rating;

    @Column( name = "special_features")
    private String specialFeatures;

    @UpdateTimestamp
    @Column(name = "last_update")
    private Timestamp lastUpdate;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "film_category",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "film_actor",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private Set<Actor> actors;

    public Film(Short filmId, String title, String description, Short releaseYear, Language language, Language originalLanguage, Byte rentalDuration, Short length, BigDecimal rentalRate, BigDecimal replacementCost, String rating, Set<Actor> actors, Set<Category> categories, String specialFeatures) {
        this.filmId = filmId;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.language = language;
        this.originalLanguage = originalLanguage;
        this.rentalDuration = rentalDuration;
        this.length = length;
        this.rentalRate = rentalRate;
        this.replacementCost = replacementCost;
        this.rating = rating;
        this.actors = actors;
        this.categories = categories;
        this.specialFeatures = specialFeatures;
    }

    public Film() {}

    public Set<Actor> getActors() {
        return actors;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public String getSpecialFeatures() {
        return specialFeatures;
    }

    public String getRating() {
        return rating;
    }

    public Short getLength() {
        return length;
    }

    public BigDecimal getRentalRate() {
        return rentalRate;
    }

    public Byte getRentalDuration() {
        return rentalDuration;
    }

    public Language getOriginalLanguage() {
        return originalLanguage;
    }

    public Language getLanguage() {
        return language;
    }

    public String getTitle() {
        return title;
    }

    public Short getFilmId() {
        return filmId;
    }

    public String getDescription() {
        return description;
    }

    public Short getReleaseYear() {
        return releaseYear;
    }

    public BigDecimal getReplacementCost() {
        return replacementCost;
    }

    public void setFilmId(Short filmId) {
        this.filmId = filmId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReleaseYear(Short releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void setOriginalLanguage(Language originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public void setRentalDuration(Byte rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    public void setRentalRate(BigDecimal rentalRate) {
        this.rentalRate = rentalRate;
    }

    public void setLength(Short length) {
        this.length = length;
    }

    public void setReplacementCost(BigDecimal replacementCost) {
        this.replacementCost = replacementCost;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setSpecialFeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    @Override
    public String toString() {
        return "Film{" +
                "filmId=" + filmId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", releaseYear=" + releaseYear +
                ", language=" + language +
                ", originalLanguage=" + originalLanguage +
                ", rentalDuration=" + rentalDuration +
                ", rentalRate=" + rentalRate +
                ", length=" + length +
                ", replacementCost=" + replacementCost +
                ", rating=" + rating +
                ", specialFeatures='" + specialFeatures + '\'' +
                ", lastUpdate=" + lastUpdate +
                '}';
    }


}
