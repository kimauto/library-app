package com.group.libraryapp.service.user;

import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.dto.response.UserResponse;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceV2 {

    private final UserRepository userRepository;

    public UserServiceV2(UserRepository userRepository) {
        this.userRepository = userRepository;

    }
    //아래 있는 함수가 시작될 떄 start transaction을 해준다
    //함수가 예외 없이 잘 끝났다면 commit
    //문제가 있다면 rollback
    @Transactional
    public void saveUser(UserCreateRequest request) {
       User u = userRepository.save(new User(request.getName(), request.getAge()));
     }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll(); // user객체를 가져옴
        return users.stream() //user 객체가 나온것을 UserResponse로 바꿔주고 전체리스트로 변경후 반환
                .map(user -> new UserResponse(user.getId(), user.getName(), user.getAge())).collect(Collectors.toList());
    }

    @Transactional
    public void updateUser(UserUpdateRequest request){
        User user  =userRepository.findById(request.getId())
                .orElseThrow(IllegalArgumentException::new);
        user.updateName(request.getName());
//        userRepository.save(user); <-- 영속성 컨테스트가 트랜잭션과 시작하고 종료되어 변경감지(dirty check)로 코드를 생략 가능
    }

    @Transactional
    public void deleteUser(String name){
        User user = userRepository.findByName(name)
                .orElseThrow(IllegalArgumentException::new);

        userRepository.delete(user);
    }



}
