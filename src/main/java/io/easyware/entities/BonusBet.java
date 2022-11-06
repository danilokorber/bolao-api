package io.easyware.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class BonusBet {
    private List<String> first;
    private List<String> second;
    private List<String> third;
    private List<String> fourth;
}
