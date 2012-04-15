% Lily was here -- automatically converted by /usr/bin/midi2ly from TestResults.mid
\version "2.7.18"
\include "english.ly"


trackAchannelA = \relative c {
  
  \tempo 4 = 30 
  c'4. g'8 g4 c, |
  % 2
  e f g2 |
  % 3
  c4. b8 a b4. |
  % 4
  a8 g d'4 e c |
  % 5
  
}

trackA = <<
  
  \clef treble
  
  \context Voice = channelA \trackAchannelA
>>


trackBchannelA = \relative c {
  s1*4 c''2 g4. b8 |
  % 6
  a2 d,4 c2 e4. e8 f2 c4 
}

trackB = <<
  
  \clef treble
  
  \context Voice = channelA \trackBchannelA
>>


trackCchannelA = \relative c {
  s1*4 c''8 a4 f a8 c b4 a8 g f f d4 c c8 c e d d4 e8 |
  % 8
  f d4 g, c8 
}

trackC = <<
  
  \clef treble
  
  \context Voice = channelA \trackCchannelA
>>


trackDchannelA = \relative c {
  s1*4 c''2 g4. b8 |
  % 6
  a2 d,4 c2 e4. e8 f2 c4 
}

trackD = <<
  
  \clef treble
  
  \context Voice = channelA \trackDchannelA
>>


trackEchannelA = \relative c {
  s1*4 c''2 g4. b8 |
  % 6
  a2 d,4 c |
  % 7
  c2 d8 e4. |
  % 8
  b2 
}

trackE = <<
  
  \clef treble
  
  \context Voice = channelA \trackEchannelA
>>


trackF = <<
>>


trackG = <<
>>


trackH = <<
>>


trackI = <<
>>


trackJ = <<
>>


trackK = <<
>>


trackL = <<
>>


trackM = <<
>>


trackN = <<
>>


trackO = <<
>>


trackP = <<
>>


\score {
  <<
    \context Staff=trackA \trackA
    \context Staff=trackB \trackB
    \context Staff=trackC \trackC
    \context Staff=trackD \trackD
    \context Staff=trackE \trackE
  >>
}
