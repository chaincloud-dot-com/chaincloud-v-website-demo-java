package com.server.web;

import com.server.model.*;
import com.server.dao.AddressDao;
import com.server.dao.WithdrawDao;
import com.server.model.Withdraw;
import org.hamcrest.core.StringContains;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(VDeviceRest.class)
public class VDeviceRestTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private WithdrawDao withdrawDao;
    @MockBean
    private AddressDao addressDao;

    @Test
    public void testGet() throws Exception {
        final Withdraw withdraw = new Withdraw(1L, "aa", "bb", 4234234234324L, 1);
        given(this.withdrawDao.findOneByStatus(TxStatus.init))
                .willReturn(withdraw);
        this.mvc.perform(get("/api/v1/open/vtest")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(new StringContains("bb")));
    }

    @Test
    public void testPost() throws Exception {
        final Withdraw withdraw = new Withdraw(1L, "aa", "bb", 4234234234324L, 1);
        given(this.withdrawDao.findById(1L))
                .willReturn(withdraw);
        final String urlTemplate = "/api/v1/open/vtest";

        this.mvc.perform(post(urlTemplate).param("vtest_id", "1").param("tx_hash", "dfdfdf")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(new StringContains("true")));

        this.mvc.perform(post(urlTemplate).param("vtest_id", "1").param("tx_hash", "dfdfdf")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(new StringContains("withdraw is completed")));

        this.mvc.perform(post(urlTemplate).param("vtest_id", "2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(new StringContains("withdraw is null")));
    }


    @Test
    public void testBatchGet() throws Exception {
        final BtcAddress vDevice1 = new BtcAddress(1L, 0L, AddressType.coldReceive, "1Gdd7xsSqJE1uH16YZca7vkKssPoShRFyw", 0L);
        final BtcAddress vDevice2 = new BtcAddress(1L, 0L, AddressType.coldReceive, "1PsdHa2geM5jzHgFbndzCyyDyh8J1eYzbN", 1L);
        List<BtcAddress> btcAddressList = new ArrayList<>();
        btcAddressList.add(vDevice1);
        btcAddressList.add(vDevice2);

        given(this.addressDao.findByStatus(AddressStatus.init))
                .willReturn(btcAddressList);

        this.mvc.perform(get("/api/v1/open/batch")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(new StringContains("1Gdd7xsSqJE1uH16YZca7vkKssPoShRFyw")));
    }

    @Test
    public void testBatchPost() throws Exception {
        given(this.addressDao.update(0L, AddressStatus.verified, AddressType.coldReceive)).willReturn(1);
        final String urlTemplate = "/api/v1/open/batch";

        this.mvc.perform(post(urlTemplate).param("batch_no", "0").param("status", "2").param("type", "2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(new StringContains("true")));

        this.mvc.perform(post(urlTemplate).param("batch_no", "1").param("status", "4").param("type", "2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(new StringContains("false")));

        this.mvc.perform(post(urlTemplate).param("batch_no", "1").param("status", "2").param("type", "3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(new StringContains("false")));

    }


}