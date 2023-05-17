package th.co.prior.market.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountModel {
    private Integer id;
    private String name;
    private Double cashAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime accountDate;
}
