package com.group.libraryapp.domain.user;

import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity// 스프링이 user객체와 user 테이블을 같은 것으로 바라본다.
//@Table(name="UERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //primary key는 자동 생성되는 값이다.
    private Long id = null;

    @Column(nullable = false, length = 20)
    private String name;

    private Integer age;
    //연관관계의 주인이 아닌 객체는 mappedBy를 통해 주인에게 매여있음을 표시해주어야한다.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)// 객체간의 관계가 끊어진 데이터를 자동으로 제거하는 옵션
    private List<UserLoanHistory> userLoanHistories = new ArrayList<UserLoanHistory>();

    protected User() {
    } //jpa는 기본 생성자가 꼭 필요하다

    public User(String name, Integer age) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다.", name));
        }
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Long getId() {
        return id;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void loanBook(String bookName) {
        this.userLoanHistories.add(new UserLoanHistory(this, bookName));
    }

    public void returnBook(String bookName) {
        UserLoanHistory targetHistory = this.userLoanHistories.stream()
                .filter(history -> history.getBookName().equals(bookName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        targetHistory.doReturn();
    }
}
