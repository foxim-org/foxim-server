package com.hnzz.dto;

import com.hnzz.entity.Contacts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author HB on 2023/3/9
 * TODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactsPage {
    private Page<Contacts> page;
    private List<ContactsDTO> contactsDTOList;
}
