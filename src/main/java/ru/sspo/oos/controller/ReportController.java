package ru.sspo.oos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sspo.oos.dto.FinancialReport;
import ru.sspo.oos.service.ReportService;

import java.time.LocalDateTime;

/**
 * Контроллер для формирования отчётов.
 * Реализует выходные данные процесса 3.0 "Организация доставки" — отчёт для бухгалтерии.
 */
@Tag(name = "Отчёты", description = "API для формирования финансовых отчётов для бухгалтерии")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "Получить финансовый отчёт за период", description = "Возвращает финансовый отчёт за указанный период. Если период не указан, возвращает отчёт за сегодня")
    @GetMapping("/financial")
    public ResponseEntity<FinancialReport> getFinancialReport(
            @Parameter(description = "Начало периода (формат: ISO_DATE_TIME)") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Конец периода (формат: ISO_DATE_TIME)") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        FinancialReport report;

        if (startDate == null && endDate == null) {
            report = reportService.getTodayFinancialReport();
        } else if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Нужно указать и startDate, и endDate в формате ISO_DATE_TIME");
        } else {
            if (endDate.isBefore(startDate)) {
                throw new IllegalArgumentException("endDate не может быть раньше startDate");
            }
            report = reportService.getFinancialReport(startDate, endDate);
        }

        return ResponseEntity.ok(report);
    }

    @Operation(summary = "Получить финансовый отчёт за сегодня", description = "Возвращает финансовый отчёт за текущий день")
    @GetMapping("/financial/today")
    public ResponseEntity<FinancialReport> getTodayReport() {
        return ResponseEntity.ok(reportService.getTodayFinancialReport());
    }

    @Operation(summary = "Получить финансовый отчёт за месяц", description = "Возвращает финансовый отчёт за текущий месяц")
    @GetMapping("/financial/monthly")
    public ResponseEntity<FinancialReport> getMonthlyReport() {
        return ResponseEntity.ok(reportService.getMonthlyFinancialReport());
    }
}

