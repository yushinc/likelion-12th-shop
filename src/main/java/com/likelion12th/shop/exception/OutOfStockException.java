package com.likelion12th.shop.exception;

public class OutOfStockException extends RuntimeException {

    // 함수 안에서 예외를 부르는 경우이기에, 함수 실행 중에 나오는 거라 RuntimeException 발생시키는 것
    public OutOfStockException(String message){
        super(message);
    }
}
