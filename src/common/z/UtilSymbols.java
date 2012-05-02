package common.z;

/**
 * Provides utilities related to symbols in Z that CZT uses.
 * @author Pablo Rodriguez Monetti
 */
public class UtilSymbols {
    
    private static int codePoints[][] = {  {8242},  // Prime symbol   0
                                           {8484},  // ℤ - Integers symbol 1
                                           {8469},  // ℕ - Naturals symbol 2
                                           {55349}, // Arithmos 1  3
                                           {56632}, // Arithmos 2 4 
                                           {56637}, // Set of finite subsets symbol  5
                                           {8594},  // → - Total function symbol     6                                  
                                           {8596},  // ↔ - Relation symbol 7
                                           {8696},  // Partial function symbol 8
                                           {10516}, // Partial injection symbol 9
                                           {8611},  // ↣ - Total injection symbol 10
                                           {10496}, // Partial surjection symbol  11
                                           {8608},  // ↠ - Total surjection symbol   12   
                                           {10518}, // Bijection symbol  13 
                                           {8699},  // Finite partial function symbol 14
                                           {10517}, // Finite partial injection symbol 15
                                           {10814}, // Forward relational composition symbol  16
                                           {8728},  // Backward relational composition symbol ∘  17
                                           {8709},  // ∅ - Empty set symbol  18
                                           {10216}, // Left angle symbol   19
                                           {10217}, // Right angle symbol  20
                                           {916},   // Δ - Delta symbol  21
                                           {926},   // Ξ - Xi symbol     22
                                           {920},   // Θ - Theta symbol  23
                                           {8713},  // ∉ - Non-membership symbol   24
                                           {8745},  // ∩ - Intersection symbol       25                                     
                                           {8746},  // ∪ - Union symbol       26
                                           {8834},  // ⊂ - Subset symbol  27
                                           {8838},  // ⊆ - Subseteq symbol   28
                                           {8726},  // ∖ - Setminus symbol   29
                                           {8854},  // ⊖ - Symdiff symbol   30
                                           {8898},  // ⋂ - Big Intersection symbol    31                                          
                                           {8899},  // ⋃ - Big Union symbol   32
                                           {8614},  // ↦ - Maplet symbol    33
                                           {8722},  // − - Minus symbol    34
                                           {8800},  // ≠ - Unequality  symbol   35                                     
                                           {8804},  // ≤ - Less or equal symbol   36
                                           {8805},  // ≥ - Greater or equal symbol    37                                 
                                           {9665},  // Domain restriction symbol      38
                                           {9655},  // Range restriction symbol       39
                                           {10852}, // Domain anti-restriction symbol   40     
                                           {10853}, // Range anti-restriction symbol    41
                                           {10631}, // Left relational image symbol     42
                                           {10632}, // Right relational image symbol    43
                                           {8853},  // ⊕ - Override symbol              44
                                           {8598},  // ↖ - RightDown-LeftUp arrow symbol    45
                                           {8599},  // ↗ - LeftDown-RightUp arrow symbol    46
                                           {8600},  // ↘ - LeftUp-RightDown arrow symbol    47
                                           {8601},  // ↙ - RightUp-LeftDown arrow symbol   48
                                           {8256},  // ⁀ - Cat symbol   49
                                           {8639},  // ↿ - Sequence extraction symbol   50
                                           {8638},  // ↾ - Sequence filtering symbol    51        
                                           {8473},  // ℙ - power symbol  52
                                        };
    
    private static String[] symbols = { new String(codePoints[0],0,1), 
                                        new String(codePoints[1],0,1),
                                        new String(codePoints[2],0,1),
                                        new String(codePoints[3],0,1) + new String(codePoints[4],0,1),
                                        new String(codePoints[2],0,1) + new String(codePoints[47],0,1) + "1" + new String(codePoints[45],0,1),
                                        new String(codePoints[52],0,1) + " _ " + new String(codePoints[47],0,1) + "1" + new String(codePoints[45],0,1),
                                        new String(codePoints[3],0,1) + new String(codePoints[5],0,1) + " _ " + new String(codePoints[47],0,1) + "1" + new String(codePoints[45],0,1),      
                                        " _ " + new String(codePoints[7],0,1) + " _ ",
                                        " _ " + new String(codePoints[6],0,1) + " _ ",
                                        new String(codePoints[3],0,1) + new String(codePoints[5],0,1) + " _ ",
                                        " _ " + new String(codePoints[8],0,1) + " _ ",  
                                        " _ " + new String(codePoints[9],0,1) + " _ ",   
                                        " _ " + new String(codePoints[10],0,1) + " _ ",                                          
                                        " _ " + new String(codePoints[11],0,1) + " _ ",   
                                        " _ " + new String(codePoints[12],0,1) + " _ ",   
                                        " _ " + new String(codePoints[13],0,1) + " _ ",                                          
                                        " _ " + new String(codePoints[14],0,1) + " _ ",   
                                        " _ " + new String(codePoints[15],0,1) + " _ ",    
                                        " _ .. _ ",
                                        " _ " + new String(codePoints[35],0,1) + " _ ",  
                                        " _ " + new String(codePoints[24],0,1) + " _ ",                                             
                                        new String(codePoints[18],0,1),
                                        " _ " + new String(codePoints[25],0,1) + " _ ",
                                        " _ " + new String(codePoints[26],0,1) + " _ ",
                                        " _ " + new String(codePoints[27],0,1) + " _ ",    
                                        " _ " + new String(codePoints[28],0,1) + " _ ",
                                        " _ " + new String(codePoints[29],0,1) + " _ ",
                                        " _ " + new String(codePoints[30],0,1) + " _ ",
                                        new String(codePoints[31],0,1),
                                        new String(codePoints[32],0,1),    
                                        " _ " + new String(codePoints[33],0,1) + " _ ", 
                                        "dom",
                                        "ran",
                                        "id _",
                                        " _ " + new String(codePoints[16],0,1) + " _ ",                                        
                                        " _ " + new String(codePoints[17],0,1) + " _ ",  
                                        " _ " + new String(codePoints[38],0,1) + " _ ",
                                        " _ " + new String(codePoints[39],0,1) + " _ ",
                                        " _ " + new String(codePoints[40],0,1) + " _ ",
                                        " _ " + new String(codePoints[41],0,1) + " _ ",                                        
                                        " _ " + new String(codePoints[42],0,1) + " _ " + new String(codePoints[43],0,1),
                                        " _ " + new String(codePoints[44],0,1) + " _ ",  
                                        new String(codePoints[46],0,1) + "+" + new String(codePoints[48],0,1) + "R",
                                        new String(codePoints[46],0,1) + "*" + new String(codePoints[48],0,1) + "R",
                                        "disjoint _ ",
                                        " _ partition _ ",
                                        "- _ ",
                                        " _ < _ ",
                                        " _ > _ ",                                      
                                        " _ " + new String(codePoints[36],0,1) + " _ ",                                          
                                        " _ " + new String(codePoints[37],0,1) + " _ ", 
                                        " _ + _ ",
                                        " _ * _ ",                                        
                                        " _ − _ ",                                        
                                        " _ div _ ",
                                        " _ mod _ ",
                                        "# _ ",
                                        "seq _ ",
                                        "iseq _ ",
                                        "seq _ " + new String(codePoints[47],0,1) + "1" + new String(codePoints[45],0,1),
                                        new String(codePoints[19],0,1) + " ,, " + new String(codePoints[20],0,1),
                                        " _ " +  new String(codePoints[49],0,1) +" _ ",
                                        " _ " +  new String(codePoints[50],0,1) +" _ ",                                        
                                        " _ " +  new String(codePoints[51],0,1) +" _ ",
                                        " _ preffix _ ",
                                        " _ suffix _ ",
                                        " _ infix _ ",  
                                        new String(codePoints[49],0,1) + "/",
                                        "rev",
                                        "head",
                                        "tail",
                                        "last",
                                        "front",
                                        "squash",
                                        "first",
                                        "second",
                                        "succ _ ",
                                        "min _ ",
                                        "max _ ",
                                        new String(codePoints[21],0,1),
                                        new String(codePoints[22],0,1),
                                        new String(codePoints[23],0,1)                            
    
    };

    

    
    
    /**
     * Prints all symbols represented in this class.
     * @param args
     */  
	public static void main (String args[]){
        System.out.println("Codepoints:");
        for(int i=0; i<codePoints.length; i++)
            System.out.println(new String(codePoints[i],0,1));
        System.out.println("Symbols:");        
        for(int i=0; i<symbols.length; i++)
            System.out.println(i + " " + symbols[i]);        
    }
    
    
    
    /**
     * Returns the number of represented symbols.
     * @return the number of represented symbols.
     */
    public static int getNumOfSymbols(){
        return symbols.length;
    }
    
    
    
    /**
     * Returns the indicated symbol.
     * @return the indicated symbol.
     */
    public static String getSymbol(int i){
        return symbols[i];
    }
    
    /*
    /**
     * Returns the prime symbol.
     * @return the prime symbol.
     */
    public static String primeSymbol(){
        return new String(symbols[0]);    
    }
    
    
    /**
     * Returns the integer symbol.
     * @return the integer symbol.
     */
    public static String integerSymbol(){
        return new String(symbols[1]);    
    }

    
   /**
    * Returns the natural symbol.
    * @return the natural symbol.
    */ 
   public static String naturalSymbol(){
        return new String(symbols[2]);    
    }


   /**
    * Returns the arithmos symbol.
    * @return the arithmos symbol.
    */
   public static String arithmosSymbol(){
        return new String(symbols[3]);
    }
    
    
   /**
    * Returns the relation symbol.
    * @return the relation symbol.
    */
    public static String relationSymbol(){
        return new String(symbols[7]);    
    } 
       
    
    /**
     * Returns the partial function symbol.
     * @return the partial function symbol.
     */
    public static String partialFunctionSymbol(){
        return new String(symbols[10]);    
    } 
     
    
   /**
    * Returns the total function symbol.
    * @return the total function symbol.
    */
    public static String totalFunctionSymbol(){
        return new String(symbols[8]);    
    } 
    
    
   /**
    * Returns the empty set symbol.
    * @return the empty set symbol.
    */
    public static String emptySetSymbol(){
        return new String(symbols[21]);    
    }
    
    
    /**
     * Returns the sequence angles symbol.
     * @return the sequence angles symbol.
     */
    public static String seqAnglesSymbol(){
        return new String(symbols[60]);    
    }
      

    
    
     /**
     * Returns the FromTo angle symbol.
     * @return the FromTo angle symbol.
     */
    public static String fromToSymbol(){
        return new String(symbols[18]);    
    }   
    
    
     /**
     * Returns the dom symbol.
     * @return the dom symbol.
     */
    public static String domSymbol(){
        return new String(symbols[31]);    
    }      
    
    
     /**
     * Returns the ran symbol.
     * @return the ran symbol.
     */
    public static String ranSymbol(){
        return new String(symbols[32]);    
    } 

     /**
     * Returns the intersection symbol.
     * @return the intersection symbol.
     */
    public static String intersectionSymbol(){
        return new String(symbols[22]);    
    } 

     /**
     * Returns the union symbol.
     * @return the union symbol.
     */
    public static String unionSymbol(){
        return new String(symbols[23]);    
    } 

     /**
     * Returns the greater or equal than symbol.
     * @return the greater or equal than symbol.
     */
    public static String gecSymbol(){
        return new String(symbols[50]);    
    } 

     /**
     * Returns the less or equal than symbol.
     * @return the less or equal than symbol.
     */
    public static String leqSymbol(){
        return new String(symbols[49]);    
    } 

     /**
     * Returns the greater than symbol.
     * @return the greater than symbol.
     */
    public static String greaterThanSymbol(){
        return new String(symbols[48]);    
    } 

     /**
     * Returns the less than symbol.
     * @return the less than symbol.
     */
    public static String lessThanSymbol(){
        return new String(symbols[47]);    
    } 

     /**
     * Returns the unequality symbol.
     * @return the unequality symbol.
     */
    public static String neqSymbol(){
        return new String(symbols[19]);
    } 
     /**
     * Returns the sequence symbol.
     * @return the sequence symbol.
     */
    public static String sequenceSymbol(){
        return new String(symbols[57]);
    } 

}
