\version "2.12.1"

\score {
  
  \relative c''' {
    \time 4/4
    \key bes \major
    
    cis16 d f,8 bes16 bes8.
    cis16 d f,8 aes16 aes8.
    f16 g cis,8  d16 f f,8
    aes16 c c, d f f8.
    
    
  }
  
  \midi 
  {
    %tempoWholesPerMinute = #(ly:make-moment 120 4)
  }
  
  \layout {
    
  }
}