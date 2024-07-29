package com.example.cafekiosk.unit;

import com.example.cafekiosk.unit.beverage.Americano;
import com.example.cafekiosk.unit.beverage.Beverage;
import com.example.cafekiosk.unit.beverage.Latte;
import com.example.cafekiosk.unit.order.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CafeKioskTest {

//    @DisplayName("음료 1개 추가 테스트")
    @DisplayName("음료 1개를 추가하면 주문 목록에 담긴다.")
    @Test
    void add_manual_test() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println(">>> 담긴 음료 수 : " + cafeKiosk.getBeverageList().size());
        System.out.println(">>> 담긴 음료 : "+ cafeKiosk.getBeverageList().get(0).getName());
    }

    @Test
    void add(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

//        assertThat(cafeKiosk.getBeverageList().size()).isEqualTo(1);
        assertThat(cafeKiosk.getBeverageList()).hasSize(1);
        assertThat(cafeKiosk.getBeverageList().get(0).getName()).isEqualTo("아메리카노");
    }

    @Test
    void addSeveralBeverages(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 2);

        assertThat(cafeKiosk.getBeverageList().get(0)).isEqualTo(americano);
        assertThat(cafeKiosk.getBeverageList().get(1)).isEqualTo(americano);
    }

    @Test
    void addZeroBeverage(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();


        assertThatThrownBy(() -> {
            cafeKiosk.add(americano, 0);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 1잔 이상 주문하실 수 있습니다");
    }
    @Test
    void remove(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        assertThat(cafeKiosk.getBeverageList()).hasSize(1);
        cafeKiosk.remove(cafeKiosk.getBeverageList().get(0));

        assertThat(cafeKiosk.getBeverageList()).isEmpty();
    }

    @Test
    void clear(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Beverage americano = new Americano();
        Beverage latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);
        assertThat(cafeKiosk.getBeverageList()).hasSize(2);

        cafeKiosk.clear();
        assertThat(cafeKiosk.getBeverageList()).isEmpty();
    }

    @Test
    void calculateTotalPrice(){
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Beverage americano = new Americano();
        Beverage latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        // when
        int totalPrice = cafeKiosk.calculateTotalPrice();

        // then
        assertThat(totalPrice).isEqualTo(8500);
    }
    @Test
    void createOrderWithCurrentTime(){
       CafeKiosk cafeKiosk = new CafeKiosk();
       Americano americano = new Americano();

       cafeKiosk.add(americano);

       Order order = cafeKiosk.createOrder(LocalDateTime.of(2024, 7, 28, 10,0));
       assertThat(order.getBeverages()).hasSize(1);
       assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @Test
    void createOrderWithOutsideOpenTime(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        assertThatThrownBy( () -> cafeKiosk.createOrder(LocalDateTime.of(2024, 7, 28, 9,59))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("")
    @Test
    void test() {
        // given

        // when

        // then

    }
}