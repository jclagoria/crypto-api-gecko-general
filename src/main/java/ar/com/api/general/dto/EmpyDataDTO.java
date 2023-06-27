package ar.com.api.general.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class EmpyDataDTO implements Serializable {

    private final String NO_DATA = "Data not Found";

}
