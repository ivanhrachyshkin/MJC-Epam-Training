package com.epam.esm.view;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public abstract class ResponseProvider {

    public static String ACCESS_DENIED_MESSAGE = "Access is denied";
    public static String UNAUTHORIZED_MESSAGE = "Full authentication is required to access this resource";

    public String getResponseOkForGetMethod(final String url, final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
    }

    public String getResponseNotFoundForGetMethod(final String url, final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn().getResponse().getContentAsString();
    }

    public String getResponseUnauthorizedForDeleteMethod(final String url, final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()).andReturn().getResponse().getContentAsString();
    }

    public String getResponseForbiddenForDeleteMethod(final String url, final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn().getResponse().getContentAsString();
    }

    public String getResponseNotFoundForDeleteMethod(final String url, final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn().getResponse().getContentAsString();
    }

    public String getResponseUnauthorizedForPostMethod(final String url, final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()).andReturn().getResponse().getContentAsString();
    }

    public String getResponseForbiddenForPostMethod(final String url, final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url))
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn().getResponse().getContentAsString();
    }

    public String getResponseCreatedForPostMethodForObjectAsString(final String url,
                                                                   final String objectAsString,
                                                                   final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectAsString))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();
    }
}
