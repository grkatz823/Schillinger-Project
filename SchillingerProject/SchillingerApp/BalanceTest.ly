% Lily was here -- automatically converted by /usr/bin/midi2ly from fourBarMelody.mid
\version "2.7.18"


trackAchannelA = \relative c'{
  
      
  d2. a'16 e16 g16 b16 | 
  d,2. f8 a8 |
  c2 e,8 g8 b8 d8 |
  g16 a4. a4. g16 f16 e16 |
  
  \tempo 4 = 120 

}

trackA = <<
  \context Voice = channelA \trackAchannelA
>>



\score {
  <<
    \context Staff=trackA \trackA
  >>
}