\version "2.12.1"

\score {
  
  \relative c''' {
    \time 12/8
    \key ees \major
    
    g4. ~ g8 f g f4. ees4 bes8 |
    g'4 c,8 c'4 g8 bes4. aes4 g8 |
    f4. g4 d8 ees4. c4. |
    bes8 d' c bes aes d, ees2.
    
  }
  
  \midi 
  {
    %tempoWholesPerMinute = #(ly:make-moment 120 4)
  }
  
  \layout {
    
  }
}