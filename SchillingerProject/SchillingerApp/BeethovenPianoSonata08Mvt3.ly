\version "2.7.18"
\include "english.ly"

\score {
  
  \relative c'' {
    \time 4/4
    \key c \minor
    
    ef4. f8 d4. ef8 |
    c2 c8 bf c d |
    ef d ef f g4 g |
    g2. f8 g |
    
    af2 d,4 ef8 f |
    g2 c,4 c8 d |
    ef4 ef8 f d4 d8 ef
    c1
  }
  
  \midi 
  {
    %tempoWholesPerMinute = #(ly:make-moment 120 4)
  }
  
  \layout {
    
  }
}