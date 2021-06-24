package com.example.webtoonservice.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class episodeEpiPayload {
    public Integer eno;
    public String epiTitle;
    public Integer webtoonId;
    public String webtoonTitle;
}
