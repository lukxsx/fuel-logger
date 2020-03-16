package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void kortinSaldoAlussaOikein() {
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void rahanLataaminenKasvattaaSaldoaOikein() {
        kortti.lataaRahaa(240);
        assertEquals("saldo: 2.50", kortti.toString());
    }
    
    @Test
    public void saldoVaheneeOikeinJosRahaa() {
        kortti.lataaRahaa(240);
        kortti.otaRahaa(100);
        assertEquals("saldo: 1.50", kortti.toString());
    }
    
    @Test
    public void saldoEiMuutuJosEiRahaa() {
        kortti.otaRahaa(50);
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void palauttaaTrueJosRahatRiittaa() {
        assertEquals(true, kortti.otaRahaa(10));
    }
    
    @Test
    public void palauttaaFalseJosRahatEiRiita() {
        assertEquals(false, kortti.otaRahaa(50));
    }
}
