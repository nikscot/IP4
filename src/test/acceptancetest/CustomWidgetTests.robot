*** Settings ***
Library         SwingLibrary
Library         keywords.CustomWidgetKeywords


*** Keywords ***

    
    
*** Test Cases ***
Hexagon Is Selected Initially
    Start Application       edu.jsu.mcis.Main
    Select Window           Main
    Label Text Should Be    label   Hexagon
    Close Window            Main

Octagon Is Selected After Center Click
    Start Application       edu.jsu.mcis.Main
    Select Window           Main
    Click Octagon Inside
    Label Text Should Be    label   Octagon
    Close Window            Main
    
Widget Is Unchanged After Edge Click
    Start Application       edu.jsu.mcis.Main
    Select Window           Main
    Click Hexagon Outside
    Label Text Should Be    label   Hexagon
    Close Window            Main

Widget Toggles With Successive Center Clicks
    Start Application       edu.jsu.mcis.Main
    Select Window           Main
    Click Octagon Inside
    Label Text Should Be    label   Octagon
    Click Hexagon Inside
    Label Text Should Be    label   Hexagon
    Close Window            Main
