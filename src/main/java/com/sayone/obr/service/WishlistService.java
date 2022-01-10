package com.sayone.obr.service;

import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.entity.WishlistEntity;
import com.sayone.obr.exception.ErrorMessages;
import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.exception.WishlistErrors;
import com.sayone.obr.repository.BookRepository;
import com.sayone.obr.repository.UserRepository;
import com.sayone.obr.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Id;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service

public class WishlistService {
    @Autowired
    WishlistRepository wishlistRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookRepository bookRepository;

    public  void removeBookFromWishlist(Long wishlistId, Long bookId) {
        WishlistEntity wishlistEntity=wishlistRepository.findBookAndId(wishlistId,bookId);
        if(wishlistEntity==null)
            throw new UserServiceException(WishlistErrors.NOT_IN_WISHLIST.getErrorMessage());
        // Optional<WishlistEntity> wishlistEntity = wishlistRepository.findByUserId(Id);
//        WishlistEntity wishlistEntity1 = wishlistEntity.get();
//        Long wishlistId =wishlistEntity1.getWishlistId();
        wishlistRepository.deleteBook(bookId,wishlistId);
    }


    public WishlistEntity addBookToWishlist(Long Id, Long bookId) {
       Optional<WishlistEntity> wishlistEntity = wishlistRepository.findByUserId(Id);
        WishlistEntity wishlistEntity1 = new WishlistEntity();
        if (wishlistEntity.isEmpty()) {
            wishlistEntity1.setUserEntity(userRepository.findAllById(Id));
            BookEntity bookEntity = bookRepository.findByBookId(bookId).get();
            wishlistEntity1.setBookEntityList(List.of(bookEntity));
        } else {
            wishlistEntity1 = wishlistEntity.get();
            BookEntity bookEntity = bookRepository.findById(bookId).get();
            for (int i = 0; i < wishlistEntity1.getBookEntityList().size(); i++) {
                if (bookEntity == wishlistEntity1.getBookEntityList().get(i))
                    throw new UserServiceException(WishlistErrors.RECORD_EXIST.getErrorMessage());

            }
            wishlistEntity1.getBookEntityList().add(bookEntity);
        }
        return wishlistRepository.save(wishlistEntity1);
    }



    public List<BookEntity> getWishlistItems(Long Id) {
        WishlistEntity wishlistEntity =wishlistRepository.getByUserId(Id);
        if(wishlistEntity.getBookEntityList().isEmpty())
            throw new UserServiceException(WishlistErrors.NO_RECORD_FOUND.getErrorMessage());
        return wishlistEntity.getBookEntityList();
    }

    public List<Map<String,Object>> getWishlistItemsByCat(Long Id,String category) {
        WishlistEntity wishlistEntity =wishlistRepository.getByUserId(Id);

       // if(wishlistEntity.getBookEntityList().isEmpty())
            //throw new UserServiceException(WishlistErrors.NO_RECORD_FOUND.getErrorMessage());

        List<Map<String,Object>> wishlistEntity2 = wishlistRepository.findBookByCat(wishlistEntity.getWishlistId(),category);
        System.out.println(wishlistEntity2);
        return wishlistEntity2 ;

    }


}
