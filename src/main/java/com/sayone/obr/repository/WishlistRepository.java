package com.sayone.obr.repository;


import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.entity.WishlistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public interface WishlistRepository extends JpaRepository<WishlistEntity,Long> {

    @Query(value = " select * from wishlist w where w.user_id= ?1", nativeQuery = true)
    Optional<WishlistEntity> findByUserId(Long Id);

    @Query(value = "select * from wishlist w where w.user_id= ?1", nativeQuery = true)
    WishlistEntity getByUserId(Long Id);

    @Query(value = " select * from wishlist_book w where w.book_id= ?1", nativeQuery = true)
    WishlistEntity findBookById(Long bookId);

    @Query(value = " select * from wishlist w where w.user_id= ?1", nativeQuery = true)
    WishlistEntity findUserById(Long Id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM wishlist_book w WHERE w.book_id = ?1 and w.wishlist_id = ?2", nativeQuery = true)
    void deleteBook(Long bookId, Long wishlistId);

    @Query(value = " select * from wishlist_book w where w.wishlist_id = ?1 and w.book_id= ?2 ", nativeQuery = true)
    WishlistEntity findBookAndId(Long wishlistId, Long bookId);

    @Query(value = " select w.wishlist_id,b.book_name,b.genre from wishlist_book w inner join book b on b.book_id=w.book_id where w.wishlist_id = ?1 and b.genre= ?2 group by b.genre,b.book_id", nativeQuery = true)
    List<Map<String,Object>> findBookByCat(Long Id, String category);
}
