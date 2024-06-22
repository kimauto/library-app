package com.group.libraryapp.service.book;

import com.group.libraryapp.domain.book.Book;
import com.group.libraryapp.domain.book.BookRepository;
import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository;
import com.group.libraryapp.dto.book.request.BookCreateRequest;
import com.group.libraryapp.dto.book.request.BookLoanRequest;
import com.group.libraryapp.dto.book.request.BookReturnRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserLoanHistoryRepository userloanHistoryRepository;
    private final UserRepository userRepository;

    public BookService(BookRepository bookRepository, UserLoanHistoryRepository userloanHistoryRepository
    ,UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userloanHistoryRepository = userloanHistoryRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public void saveBook(BookCreateRequest request) {
        bookRepository.save(new Book(request.getName()));
    }

    public void loanBook(BookLoanRequest request) {
        // 1.책정보를 가져옴
        Book book = bookRepository.findByName(request.getBookName())
                .orElseThrow(IllegalArgumentException::new);
        // 2.대출 기록 정보를 확인해서 대출중인지 확인합니다.
        // 3.만약에 확인했는데 대출중이라면 예외를 발생시킵니다.
        if (userloanHistoryRepository.existsByBookNameAndIsReturn(book.getName(), false )) {
            throw new IllegalArgumentException("진작 대출되어 있는 책입니다.");
        }
        // 4.유저 정보를 가져온다.
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);
        // 5. 유저 정보와 책정보를 기반으로 userLoanHistory를 저장
//         userloanHistoryRepository.save(new UserLoanHistory(user, book.getName()));
            user.loanBook(book.getName());

    }

    @Transactional //반납기능
    public void returnBook(BookReturnRequest request) {
        // 1. 유저를 찾음
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);
//        // 2. 대출 기록을 찾음
//        UserLoanHistory history = userloanHistoryRepository.findByIdAndBookName(user.getId(), request.getBookName())
//                .orElseThrow(IllegalArgumentException::new);
//        // 3. 반납처리해주기
//        history.doReturn();
        user.returnBook(request.getBookName());

    }
}
