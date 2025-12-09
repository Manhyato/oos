package ru.sspo.oos.service;

import ru.sspo.oos.dto.FinancialReport;

import java.time.LocalDateTime;

/**
 * Сервис для формирования отчётов.
 * Реализует выходные данные процесса 3.0 "Организация доставки" — отчёт для бухгалтерии.
 */
public interface ReportService {

    /**
     * Получить финансовый отчёт за указанный период.
     * @param startDate Начало периода
     * @param endDate Конец периода
     * @return Финансовый отчёт
     */
    FinancialReport getFinancialReport(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Получить финансовый отчёт за сегодня.
     */
    FinancialReport getTodayFinancialReport();

    /**
     * Получить финансовый отчёт за текущий месяц.
     */
    FinancialReport getMonthlyFinancialReport();
}

