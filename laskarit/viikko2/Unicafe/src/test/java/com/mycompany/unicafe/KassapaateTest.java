package com.mycompany.unicafe;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class KassapaateTest {

    Kassapaate paate;

    @Before
    public void setUp() {
        paate = new Kassapaate();
    }

    @Test
    public void rahamaaraJaMyytyjenMaaraOnOikea() {
        assertEquals(100000, paate.kassassaRahaa());
        assertEquals(0, paate.edullisiaLounaitaMyyty());
        assertEquals(0, paate.maukkaitaLounaitaMyyty());
    }

    @Test
    public void rahamaaraKasvaaJaVaihtorahaOikeaEdullinen() {
        int vaihtoraha = paate.syoEdullisesti(260);
        assertEquals(100240, paate.kassassaRahaa());
        assertEquals(20, vaihtoraha);
    }

    @Test
    public void rahamaaraKasvaaJaVaihtorahaOikeaMaukas() {
        int vaihtoraha = paate.syoMaukkaasti(420);
        assertEquals(100400, paate.kassassaRahaa());
        assertEquals(20, vaihtoraha);
    }

    @Test
    public void myytyjenEdullistenLounaidenMaaraKasvaa() {
        paate.syoEdullisesti(240);
        assertEquals(1, paate.edullisiaLounaitaMyyty());
    }

    @Test
    public void myytyjenMaukkaidenLounaidenMaaraKasvaa() {
        paate.syoMaukkaasti(400);
        assertEquals(1, paate.maukkaitaLounaitaMyyty());
    }

    @Test
    public void josMaksuEiRiitaEiMuutoksiaEdullinen() {
        int vaihtoraha = paate.syoEdullisesti(220);
        assertEquals(100000, paate.kassassaRahaa());
        assertEquals(220, vaihtoraha);
        assertEquals(0, paate.edullisiaLounaitaMyyty());
    }

    @Test
    public void josMaksuEiRiitaEiMuutoksiaMaukas() {
        int vaihtoraha = paate.syoMaukkaasti(380);
        assertEquals(100000, paate.kassassaRahaa());
        assertEquals(380, vaihtoraha);
        assertEquals(0, paate.maukkaitaLounaitaMyyty());
    }

    // korttitestit
    @Test
    public void josKortillaRahaaOstoToimiiEdullinen() {
        Maksukortti kortti = new Maksukortti(300);
        assertEquals(true, paate.syoEdullisesti(kortti));
        assertEquals(60, kortti.saldo());
    }

    @Test
    public void josKortillaRahaaOstoToimiiMaukas() {
        Maksukortti kortti = new Maksukortti(450);
        assertEquals(true, paate.syoMaukkaasti(kortti));
        assertEquals(50, kortti.saldo());
    }

    @Test
    public void myytyjenEdullistenMaaraKasvaa() {
        Maksukortti kortti = new Maksukortti(240);
        paate.syoEdullisesti(kortti);
        assertEquals(1, paate.edullisiaLounaitaMyyty());
    }

    @Test
    public void maukkaidenEdullistenMaaraKasvaa() {
        Maksukortti kortti = new Maksukortti(400);
        paate.syoMaukkaasti(kortti);
        assertEquals(1, paate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void josEiTarpeeksiRahaaEiMuutuEdullinen() {
        Maksukortti kortti = new Maksukortti(200);
        assertEquals(false, paate.syoEdullisesti(kortti));
        assertEquals(0, paate.edullisiaLounaitaMyyty());
        assertEquals(200, kortti.saldo());
    }
    
    @Test
    public void josEiTarpeeksiRahaaEiMuutuMaukas() {
        Maksukortti kortti = new Maksukortti(380);
        assertEquals(false, paate.syoMaukkaasti(kortti));
        assertEquals(0, paate.maukkaitaLounaitaMyyty());
        assertEquals(380, kortti.saldo());
    }
    
    @Test
    public void kassaEiMuutuKortillaEdullinen() {
        Maksukortti kortti = new Maksukortti(240);
        paate.syoEdullisesti(kortti);
        assertEquals(100000, paate.kassassaRahaa());
    }
    
    @Test
    public void kassaEiMuutuKortillaMaukas() {
        Maksukortti kortti = new Maksukortti(400);
        paate.syoMaukkaasti(kortti);
        assertEquals(100000, paate.kassassaRahaa());
    }
    
    @Test
    public void kortilleLadattaessaSaldoMuuttuu() {
        Maksukortti kortti = new Maksukortti(0);
        paate.lataaRahaaKortille(kortti, 100);
        assertEquals(100, kortti.saldo());
    }
    
    @Test
    public void kortilleLadattaessaKassaKasvaa() {
        Maksukortti kortti = new Maksukortti(0);
        paate.lataaRahaaKortille(kortti, 100);
        assertEquals(100100, paate.kassassaRahaa());
    }
    
    @Test
    public void kortilleEiVoiLadataNegatiivista() {
        Maksukortti kortti = new Maksukortti(0);
        paate.lataaRahaaKortille(kortti, -50);
        assertEquals(0, kortti.saldo());
    }

}
