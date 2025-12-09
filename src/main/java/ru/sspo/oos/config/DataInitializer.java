package ru.sspo.oos.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sspo.oos.model.Courier;
import ru.sspo.oos.model.Pizza;
import ru.sspo.oos.model.PizzaCategory;
import ru.sspo.oos.repository.CourierRepository;
import ru.sspo.oos.repository.PizzaCategoryRepository;
import ru.sspo.oos.repository.PizzaRepository;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final PizzaRepository pizzaRepository;
    private final PizzaCategoryRepository categoryRepository;
    private final CourierRepository courierRepository;

    @PostConstruct
    public void init() {
        initMenu();
        initCouriers();
    }

    /**
     * Инициализация меню пицц.
     */
    private void initMenu() {
        if (pizzaRepository.count() == 0) {
            PizzaCategory classic = new PizzaCategory();
            classic.setName("Классические");
            categoryRepository.save(classic);

            Pizza p1 = new Pizza();
            p1.setName("Маргарита");
            p1.setPrice(BigDecimal.valueOf(450));
            p1.setCategory(classic);
            pizzaRepository.save(p1);

            Pizza p2 = new Pizza();
            p2.setName("Пепперони");
            p2.setPrice(BigDecimal.valueOf(520));
            p2.setCategory(classic);
            pizzaRepository.save(p2);

            PizzaCategory special = new PizzaCategory();
            special.setName("Авторские");
            categoryRepository.save(special);

            Pizza p3 = new Pizza();
            p3.setName("4 Сыра");
            p3.setPrice(BigDecimal.valueOf(600));
            p3.setCategory(special);
            pizzaRepository.save(p3);
        }
    }

    /**
     * Инициализация тестовых курьеров для MVP.
     */
    private void initCouriers() {
        if (courierRepository.count() == 0) {
            Courier courier1 = new Courier();
            courier1.setName("Иван Петров");
            courier1.setPhone("+79001111111");
            courier1.setAvailable(true);
            courierRepository.save(courier1);

            Courier courier2 = new Courier();
            courier2.setName("Сергей Сидоров");
            courier2.setPhone("+79002222222");
            courier2.setAvailable(true);
            courierRepository.save(courier2);

            Courier courier3 = new Courier();
            courier3.setName("Алексей Козлов");
            courier3.setPhone("+79003333333");
            courier3.setAvailable(true);
            courierRepository.save(courier3);
        }
    }
}

