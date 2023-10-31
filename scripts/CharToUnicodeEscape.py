char = input("Enter a character: ").strip()
if char:
    unicode_escape = f'\\u{ord(char):04X}'
    print(unicode_escape)
