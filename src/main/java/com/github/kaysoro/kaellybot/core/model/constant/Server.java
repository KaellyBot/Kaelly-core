package com.github.kaysoro.kaellybot.core.model.constant;

import com.github.kaysoro.kaellybot.core.util.Translator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Server {

    AGRIDE    ("server.agride"    ),
    ATCHAM    ("server.atcham"    ),
    CROCABULIA("server.crocabulia"),
    ECHO      ("server.echo"      ),
    MERIANA   ("server.meriana"   ),
    OMBRE     ("server.ombre"     ),
    OTO_MUSTAM("server.oto_mustam"),
    RUBILAX   ("server.rubilax"   ),
    PANDORE   ("server.pandore"   ),
    USH       ("server.ush"       ),
    JULITH    ("server.julith"    ),
    NIDAS     ("server.nidas"     ),
    MERKATOR  ("server.merkator"  ),
    FURYE     ("server.furye"     ),
    BRUMEN    ("server.brumen"    ),
    ILYZAELLE ("server.ilyzaelle" );

    private String key;

    public String getLabel(Language lang){
        return Translator.getLabel(lang, getKey());
    }
}