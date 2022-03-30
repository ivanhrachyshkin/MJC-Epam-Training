package com.epam.esm.view;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public abstract class ResponseProvider {

    public static String ACCESS_DENIED_MESSAGE = "Access is denied";
    public static String UNAUTHORIZED_MESSAGE = "Full authentication is required to access this resource";

    public String getOkForGetMethod(final String url, final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
    }

    public String getNotFoundForGetMethod(final String url, final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn().getResponse().getContentAsString();
    }

    public String getUnauthorizedForGetMethod(final String url, final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()).andReturn().getResponse().getContentAsString();
    }

    public String getForbiddenForGetMethod(final String url, final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn().getResponse().getContentAsString();
    }

    public String getBadRequestForGetMethod(final String url, final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn().getResponse().getContentAsString();
    }

    public String getOkForDeleteMethod(final String url, final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
    }

    public String getUnauthorizedForDeleteMethod(final String url, final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()).andReturn().getResponse().getContentAsString();
    }

    public String getForbiddenForDeleteMethod(final String url, final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn().getResponse().getContentAsString();
    }

    public String getNotFoundForDeleteMethod(final String url, final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn().getResponse().getContentAsString();
    }

    public String getUnauthorizedForPostMethod(final String url, final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()).andReturn().getResponse().getContentAsString();
    }

    public String getForbiddenForPostMethod(final String url, final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url))
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn().getResponse().getContentAsString();
    }

    public String getCreatedForPostMethodForObjectAsString(final String url,
                                                           final String objectAsString,
                                                           final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectAsString))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();
    }

    public String getBadRequestForPostMethod(final String url,
                                             final String objectAsString,
                                             final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectAsString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn().getResponse().getContentAsString();
    }

    public String getConflictForPostMethod(final String url,
                                           final String objectAsString,
                                           final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectAsString))
                .andExpect(MockMvcResultMatchers.status().isConflict()).andReturn().getResponse().getContentAsString();
    }

    public String getNotFoundForPostMethod(final String url,
                                           final String objectAsString,
                                           final MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectAsString))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn().getResponse().getContentAsString();
    }
}
