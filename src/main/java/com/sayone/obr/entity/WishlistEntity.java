package com.sayone.obr.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "wishlist")

public class WishlistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistId;

//    @UpdateTimestamp
//    private LocalDateTime updatedTime;
//    @CreationTimestamp
//    private LocalDateTime createTime;

    @OneToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private UserEntity userEntity;

    @ManyToMany(fetch = FetchType.EAGER ,cascade = CascadeType.PERSIST)
    @JoinTable(name = "wishlist_book",
            joinColumns = {@JoinColumn(name = "wishlist_id",referencedColumnName = "wishlistId",
                    nullable = false,updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "book_id",referencedColumnName = "bookId",
                    nullable = false,updatable = false)})
    @JsonIgnore
    private List<BookEntity>bookEntityList;

    public List<BookEntity>getBookEntityList() {
        return bookEntityList;
    }

    public void setBookEntityList(List<BookEntity> bookEntityList) {
        this.bookEntityList = bookEntityList;
    }

    public Long getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Long wishlistId) {
        this.wishlistId = wishlistId;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
//
//    public LocalDateTime getUpdatedTime() {
//        return updatedTime;
//    }
//
//    public void setUpdatedTime(LocalDateTime updatedTime) {
//        this.updatedTime = updatedTime;
//    }
//
//    public LocalDateTime getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(LocalDateTime createTime) {
//        this.createTime = createTime;
//    }

    //    public void setUserEntity(Optional<UserEntity> byId) {
//        this.userEntity=userEntity;
//    }
}