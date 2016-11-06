package com.teamten.typeset;

import com.teamten.font.FontManager;
import com.teamten.font.SizedFont;
import com.teamten.font.FontVariant;
import com.teamten.font.PdfBoxFontManager;
import com.teamten.font.Typeface;
import com.teamten.hyphen.HyphenDictionary;
import com.teamten.typeset.element.Glue;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.Arrays;

import static com.teamten.typeset.SpaceUnit.PT;

/**
 * Test app for trying various things with the typesetter. This gives us more control than using Markdown.
 */
public class TypesetterTest {
    private static final String[] PARAGRAPHS = {
            "After a minute Patrick came back with a small dusty cardboard box. Dave and I stared as Patrick opened it and pulled out a network switch, the old kind from the days when they were made with metal cases. He plugged in the power supply and carefully straightened out a CAT-5 cable to hook the switch up to our network. I wanted to yell at him for being so careful and deliberate at a time like this. Dave sat next to me and was uncharacteristically still.",
            "I stopped breathing as Patrick struggled to get the plug lined up with the port.  I stared at the front panel lights, and felt Dave doing the same. My eyes watered. Patrick pushed the plug in. The front lights immediately lit and flashed actively. I felt my hands and face flush, and out of the corner of my eye saw Dave sit up and open his mouth as if to speak. He then put his face down into his cupped hands, and threw up.",
            "This is not how we would have reacted three weeks earlier.  Three weeks earlier we had settled down into that difficult part of a project after the honeymoon ends and you have to implement the tedious parts of the product, the parts that were just simple boxes in the design, and seemed straightforward, but turned out to be tricky for reasons that are incidental, and therefore uninteresting.",
            "Patrick and Dave had done the design, of course, and had humored me as I had suggested various ideas which in retrospect must have been obvious to them. It had been fascinating to hear them debate design issues. So many of their arguments were based on intuition rather than reasoned logic. I had learned more each week than my entire last semester of school.",
            "I found myself siding more with Dave. Dave Mitchell had a great laugh. He was on the heavy side and knocked things over a lot. I liked the insights he had about management and the process of software development, and he liked to teach me things. Patrick and Dave had split up the work they had designed, and they had given me tests to write in advance of their code being ready.",
            "It had been a quiet day when I heard Dave whisper, “What the hell?” and put his hands into his hair. I sensed an opportunity to learn and walked over to his desk. He didn't notice.  He was quickly switching back to his editor, adding a line of debug output, compiling, running, shaking his head, and switching back to add another line elsewhere.",
            "“What's the matter?”",
            "He waited until the program finished running to answer. “I can't figure out this bug. I can't figure out where this number is coming from here.” He pointed to a line in this debug output. Dave and Patrick liked to debug by writing lines to the console, rather than stepping through with a debugger, as I liked to do.",
            "“What happens when you step through it in the debugger?” This was a tease. Dave didn't know how to use a debugger, short of displaying the stack trace of a core file.",
            "Dave paused, then brought up the debugger on his program. I smiled in surprise, then walked him through how to set breakpoints. We spent the next few hours narrowing down where, in the mess of his code, Boost, and the system libraries, this value was coming from.",
            "“Patrick, can you help us here?” This surprised me more than the debugger. Dave rarely asked Patrick for help.",
            "Patrick came over and Dave explained the situation. “The program is crashing, and that's because of this bad dereference here, but the value is correct here.” Dave pointed at some code while Patrick's eyes scanned the screen. Then a thought occurred to me. Dave was still explaining and I didn't want to interrupt. My heart was jumping as I waited for Dave to take a breath.",
            "“I think it's a compiler bug,” I said with confidence, squinting my eyes at the code. Patrick and Dave raced each other to brush this off.",
            "“Blaming the compiler is a last resort,” said Patrick. “Same with the standard library. Chances are vastly greater that your brand-new code has a bug, rather than code being used by thousands of people.” I nodded wisely, but I felt my cheeks blush.",
            "Patrick Karlsson often made me feel bad. He never meant to, I'm sure, but he had a way of arguing in a brutally straightforward way that made me feel thoughtless and young. He was taller than Dave and I, and thin. He was quiet, except when correcting something I had said or code I had written, but I liked him anyway. I sometimes found his design decisions questionable and naive. Dave and I liked to gang up on him.",
            "Patrick was cracking his knuckles. He asked questions about threads and volatile and aliasing, questions I didn't entirely understand but wish I'd thought of. Dave considered each and said that none of Patrick's concerns applied in this case. Patrick pursed his lips to the right, the way he did when he wasn't sure the answers he'd been given were correct, and said, “Hmmm, I don't know. Weird,” and returned to his desk.",
            "I was relieved that Patrick hadn't crushed us with an obvious solution, and I guessed that Dave felt the same as he sat back up from his slouch and started typing. “Is there any way to show the assembly right here where things go pear-shaped?”",
            "I showed him how to do this, and I wondered if he was giving my theory a chance. He typed the command and the lines of C were split apart by dozens of lines of assembly.",
            "“That's not right,” Dave said, “We got the command wrong.”",
            "“I'm sure that's right,” I said. I knew the debugger well, and by “we” he meant me.",
            "“No it's not, that far too much assembly for this line of code. And some of these aren't even legal assembly instructions.”",
            "“That's not possible,” said Patrick from his desk.",
            "Dave blinked slowly. “Well, I meant that I've never seen these, they're not what a compiler outputs, this isn't the right place.” He was flustered, and I felt more inexperienced than ever. Dave got up and said, “I'm burned out, we'll look at this again tomorrow.”",
            "He left and I sat down at his computer. The only thing I had contributed all day was knowledge about the debugger, and Dave didn't even think I had gotten that right. I looked at the assembly on the screen. Some of the instructions were obvious and some were cryptic. I found an online assembly reference and started tracing the instructions, keeping notes about register contents in my notebook.",
            "Dave was right, these instructions made no sense. Not only did they perform too much work for the corresponding C code, but they didn't even make sense internally. But I convinced myself that this was at least the right section, because a few of the instructions matched the surrounding C lines.",
            "I focused on one instruction in particular. It was subtracting a register from itself. That's not necessarily odd in itself: it might be an efficient way to set a register to zero. But this register was then used in other math instructions. The compiler must have known it'd be zero and optimized it out. I hesitated, then called Patrick over. I explained what I had found. He stared at it for a good while, then said, “That's not a normal subtraction instruction.”",
            "He was right. I looked it up more carefully, and found that it was a variation that also subtracted out the carry bit. This was a way to get the carry bit into a register. I worked my way backwards to see where the carry was set. The code got increasingly convoluted, and I repeatedly made incorrect assumptions that threw me off-course.",
            "I turned around to ask Patrick a question and found an empty desk. It was nearly midnight. I set the office alarm and rode my bike home.",
            "I got in late the next morning. I hadn't slept well, and it had taken me hours to fall sleep. Each time I closed my eyes I saw fast-moving assembly instructions in large bright letters.",
            "I walked straight to Dave's desk to discuss the previous night's investigations, but he wasn't interested.",
            "“You were right,” he said, “It was a compiler bug. I tweaked the C code and I'm no longer triggering it. That weird assembly is gone.”",
            "I was in the awkward position of having to contradict the compliment.  “I don't know if it's a bug. The code I saw wouldn't have been generated by a bug.”",
            "“It was definitely a compiler bug.” I had worked with Dave enough to know that the more confidently he spoke, the less secure he was. But he was tired of being held up by the problem, and I dropped it.",
            "Dave walked up to my desk with a grin and a cup from Peet's Coffee. I thought all coffees tasted fine, but he was picky, so I pretended to be picky so that I could spend time with him walking to Peet's. I felt a bit stung that he'd gone without me this time.",
            "“You remember that compiler bug from Tuesday?” He grinned some more. His teeth were yellow from the espresso.",
            "“Yep.”",
            "“It happened again this morning. Same file, same problem. The weird assembly is back. The strange thing is that I didn't change the C file.” He was surprisingly mellow about it.",
            "“Maybe you changed a header file?” I'm brilliant.",
            "“Oh maybe.” He frowned. “Actually, no. Let me check SVN.”",
            "He came back two minutes later. No relevant files had changed.",
            "Patrick walked over. “This is no good. Here it's a crash, but it could be something more subtle. In four months our system will be so complex that a subtle problem will take weeks to track down. Can you look at the compiler's release notes and see if we can upgrade?”",
            "I looked briefly and found what I had expected. We were on the latest and most stable version of the recommended major release. It didn't get more stable than that. I went further and searched for some permutation of “weird assembly” and “compiler bug” and found nothing.",
            "I compiled Dave's code on my computer and disassembled it. The strange code was there, at the same place it had been Tuesday. I recognized the subtract-with-carry instruction and a few others that seemed unusual. I also looked through the rest of the code. It was striking how the unusual instructions didn't appear anywhere else. The rest of the generated code used instructions you would expect. After all, when do you ever need to subtract and use the existing value of the carry bit?",
            "I downloaded the source code of the compiler. I had never been so overwhelmed. It was a tangled mess of compiler passes, plug-in frameworks, embedded languages to describe CPU architectures, and abstraction layers. I went straight to the files that described the translation from the abstract syntax tree to the machine's assembly. I grepped for the subtract-with-carry. It wasn't there. I looked for a few other odd ones. Some were there, most weren't.",
            "At lunch I mentioned my findings. The small office we used had a patio, which, it being Mountain View, was nearly always usable. Friday was Togo's day. I preferred Subway, but Dave hated the bread.",
            "“That's super weird,” said Patrick, frowning at the table. He said “super” a lot. I think it's a California thing. “Which other ones weren't in the translation file?”",
            "“I don't remember,” I said. “A few more that involved the carry bit. Some vector instructions.”",
            "Patrick looked up. “That compiler doesn't do vectorization.”",
            "“That's what I just said.”",
            "“Either the instruction was generated by mistake or you're disassembling non-code.” He was still frowning.",
            "Dave cut in. “It's definitely code. This is what's causing our bug.  It's being executed.”",
            "“And it's not generated by mistake,” I added. “It's a math instruction, but I don't think it's being used for math. It's not nonsense.”",
            "“Neat.” We had finally hooked Patrick.  After lunch he and I sat down at my desk and walked through the part of the code I knew best. I showed him how although the instructions were obscure and strangely used, they did make sense, all together. There was a deliberate flow of data.",
            "“Let's look for a backward jump instruction,” he said.",
            "“Why?”",
            "“The target of such a jump may be the top of a loop. It's a good place to start the analysis.”",
            "“You're actually going to figure out what this code is doing?”",
            "“Oh yeah.” He smiled and his eyes lit up.",
            "It took us the rest of the afternoon to pick through the convoluted jump targets and decode four consecutive instructions. That snippet, it turns out, was finding the sign of an integer. Anyone else would have done a simple comparison and a jump to set the output register to -1, 0, or 1, but the four instructions were a mess of instructions that all either set the carry bit as a side-effect, or used it in an unorthodox way.",
            "“You know,” said Patrick, “this isn't even the interesting part. I want to know how this code gets in here. You said these instructions weren't in the translation file?”",
            "“That's right.”",
            "“They must be elsewhere. Let's grep the source for both the symbolic version and the op-code.”",
            "I did a recursive grep and it came up empty. I didn't know what to try next.",
            "“Try the binary,” said Patrick.",
            "“What binary?”",
            "“The binary of the compiler.”",
            "I grepped for the name of the assembly instruction and found nothing, but looking for the op-code turned up hundreds of hits.",
            "“Interesting,” I said, “they're not generating individual instructions, they're dumping chunks of pre-built code.”",
            "“Why?”",
            "“I don't know why.”",
            "“I mean, why do you say that?”",
            "“Because these op-codes are all together. It's not a look-up table.”",
            "“Maybe it's code,” he said after a second.",
            "I disassembled the whole compiler and looked for a few of the suspicious instructions, and found large sections that looked like the obscure code we had been tracing all afternoon.",
            "“The compiler is infected!” I said. “No wonder we couldn't find it in its source code.”",
            "“Okay, let's start by recompiling the compiler,” said Patrick. “I'll poke around the Web to see if anyone's seen this before. When you're done with the compiler, recompile all our own code and see if Dave's bug is gone.”",
            "The compiler took two hours to recompile, not including the time I spent learning the convoluted build process. Meanwhile, Patrick found nothing online. I rebuilt our source tree and ran the tests. They failed, in the same place.",
            "“Maybe the bug was due to something else after all?” I suggested when Patrick walked over.",
            "“Disassemble the compiler again,” he said.",
            "I did, and the foreign codes were still there.",
            "“That can't be,” I said. “This is a fresh build of the official sources.”",
            "“They must have gotten infected. Perhaps someone broke into the download site and replaced them with modified sources.”",
            "“I would love to see that code,” I said, and opened my editor to a file in the compiler.",
            "“That's going to take you forever,” Patrick said. “Put a breakpoint in write(), with a condition of the string containing this op-code.”",
            "And this from the guy who pretends not to like debuggers! Setting the condition was not easy, and there were many false positives, but the following Monday morning I hit the write() that outputs the suspicious code. I popped up to the C code. It was a generic routine to output a buffer. I worked backwards to the code that had filled it, and it was all straightforward loops based on the translation table, which I knew was clean.",
            "In desperation, I started paging through every source file of the compiler looking for any code that might be responsible. Much of it was manipulating the abstract syntax tree. Then it occurred to me that the hack couldn't be there, since the back-end translation table was clean. The hack must be in the back-end itself. In fact, it would have to be after register assignment. This narrowed down the search considerably, and I spent an hour looking through these files before it was time for lunch.",
            "Monday was pho day. We always went to Pho World, which was so low-rent it didn't even have a menu, but it was a delicious way to spend four dollars.",
            "“Eye of round, no tripe, no cilantro,” said Dave to the little man, and we all followed suit. I don't know what cut “eye of round” is but I know it's safe.",
            "“So I couldn't find it,” I said, “either backward from the breakpoint or forward from the code.”",
            "“Are you still debugging that?” said Dave.",
            "“We have to,” said Patrick. “We can't build a product on a foundation like this.”",
            "“It's just so odd that the C source would be clean but the assembly have these weird op-codes,” I said.",
            "“I thought you had tracked it down to the compiler,” said Dave.",
            "“I don't mean in your code, I mean in the compiler itself.”",
            "“The compiler could be responsible for that too.”",
            "“No, I checked the code.”",
            "“But the code is compiled by the compiler.”",
            "I didn't understand what Dave was saying, but Patrick had looked up and seemed on the verge of an insight. I waited for the lucid explanation.",
            "“So the compiler detects and modifies your program, for reasons still unknown, but also detects and modifies itself when compiled.”",
            "“Right,” said Dave with a grin, squirting brown sauce in his soup and splattering some of it onto the table.",
            "“How would that work?” Patrick said, now in full form. “A hacker adds this code to the compiler and distributes it to everyone. The code detects that it's compiling the compiler and adds itself back into the binary. One revision later the hacker removes the code from the official source distribution. The hack then perpetuates itself forever, with no trace in the source.”",
            "“And what's the purpose?” I said. I was skeptical that code could detect and insert itself so flawlessly across multiple versions.",
            "“I don't know, we didn't get far enough into the analysis of Dave's code. The obvious thing would be some kind of password validation code, modified to always accept some back-door password.”",
            "I wish he wouldn't say that such things are obvious.",
            "“So let's go back to an old version of the compiler,” said Dave.",
            "“You mean the binary of an old version. The source won't help us. I don't think we have binaries around. We don't know how far back this goes.”",
            "“How about this,” I said, trying to be helpful, “I'll write a utility that you can run on a binary, and it'll tell you if it detects the suspicious use of these op codes.”",
            "“Oh good idea.” I liked Patrick.",
            "It didn't take long to write this utility. It just ran a binary through the disassembler, then through a few greps to find instructions that were definitely not generated by the compiler. It found the code in Dave's program, as well as in the compiler. I set it loose on all executables in the system.",
            "“Here's the list,” I said as I walked to Patrick's desk. It was three printed pages. He looked through it.",
            "“This is not good. The Java runtime, the Python runtime, Perl, the compiler, and a bunch of other programs that probably don't matter.”",
            "“Why do those runtimes matter?”",
            "“Because if we can't trust the C compiler, then we have to write a new one, which isn't all that hard, but what language are you going to write it in? Are you going to trust your new compiler to the hacked Python interpreter?”",
            "“You're not going to write a new compiler,” said Dave from his desk. “Don't get all conspiracy paranoid. It's probably just a bug.”",
            "I left Patrick staring at the list and went back to my desk. I still didn't have an answer to the question I had asked at lunch: What's the purpose of this hack? I had enjoyed reverse-engineering some of these bits of code, and frankly I feared that Patrick would ask me to write a C compiler in assembly, so I put my headphones on and opened the debugger to the part of the C compiler that looked obfuscated.",
            "Again the over-use of instructions that involve the carry bit, unusual abuse of vector instructions, and convoluted and sometimes unnecessary jumps. No compiler would generate this. This was hand-crafted to be difficult to understand. I set out to figure out the purpose of this page of code.",
            "Some time later I saw the custodian pick up my trash bin to empty it. I took off my headphones. Dave and Patrick were gone. It was ten o'clock. I looked back at the screen of now-familiar instructions. I had pieced together a rough idea of what it did, or at least what some of its parts did. I felt a rush of energy. I was close to an answer. I stayed until morning.",
            "Patrick sat next to me as I pointed at my screen. “Here they use the vector instruction to get a sum of squares, which is just a convoluted way to compare these two byte arrays.” That was the climax of a ten-minute explanation.",
            "“Wait, so what are they doing?” he asked.",
            "“They're doing pattern matching.”",
            "“Why all the convoluted crap?”",
            "“Well it's a fuzzy search, and I guess it's pretty fast.”",
            "“Yeah but this is the most convoluted Rube-Goldberg way of doing something I've ever seen.”",
            "My shoulders sunk and I fiddled with the mouse. I looked at my reams of notes. He wasn't even a bit impressed. ",
            "“So, what now?” I asked after several seconds of silence.",
            "“I don't know, let me catch up on email.” He left for his desk. I was deflated and my muscles ached. I spent the rest of the morning browsing feeds and refreshing the live blog of a MacWorld event.",
            "At lunch Patrick recounted my findings to Dave. He remembered every detail. Dave grinned and shook his head with every new complication in the algorithm. I wasn't convinced that he was able to follow it all so quickly, and without any code to look at. I realized, listening to it all again, that Patrick had been right. This was far too convoluted a way of doing something relatively simple.",
            "“Have you ever seen those obfuscated programming contests?” Dave asked when Patrick was almost finished. “This is just like that.”",
            "“A friend and I competed with each other to write an obfuscated program in college,” said Patrick. “I don't know how he did it, but I started by writing the program normally, then incrementally made it more difficult to follow, by renaming variables, inlining expressions, stuff like that. That process never would have resulted in what I saw this morning.”",
            "“Well maybe other people do it differently,” said Dave.",
            "“I feel strange about this code,” said Patrick. “I don't know how to explain it. It feels cold.”",
            "Dave and I looked at Patrick. He was dissolving wasabi into his soy sauce. I didn't interrupt his thought-gathering.",
            "“It's like those chess programs,” he finally said. “They don't have intuition about what will work, or feelings about the board position, or experience to draw on. They just try every possible option and pick the best one. This feels like that. Like someone tried every combination of instructions until they got code that did what it was supposed to do. There's no beauty. The code is ugly and functional. I guess that's what I meant by `cold'.”",
            "“How does it know what it's supposed to do?” I asked.",
            "“Well,” said Patrick, “maybe you could just define boundary cases and run the code to see if the output was right. Code that works on boundary cases is likely to work for all cases.”",
            "“Oh I like that!” said Dave, grinning. “I would love to specify the outcome and have the compiler write the code for me.”",
            "“Well specifying the outcome precisely enough would be no easier than just writing the code,” said Patrick, and I felt my smile fade with Dave's. I wondered whether a drunk Patrick would be more fun. Or at least less of a buzz-kill.",
            "“So why would somebody do that, then?” I asked.",
            "Patrick put a roll into his mouth and stared at the center of the round patio table. “Maybe no one did. Maybe this is all computers doing this.”",
            "I didn't know what to say to that. I didn't think he was kidding.",
            "“Okay, there's this thing called Occam's Razor,” Dave started.",
            "“Well what's your explanation?”",
            "“Um, not self-aware artificially-intelligent robot overlords infecting my object marshalling code.”",
            "“That's not an explanation.”",
            "“Have you run an anti-virus scan?” Dave liked to pronounce it “an-TIV-erus”, like the name of a Roman emperor.",
            "“Does that affect your explanation of why this code looks this way?” Patrick could be a bit brutal.",
            "Dave exhaled and took the question seriously. It was hard to refute Patrick's point. No other explanation made sense. We had never seen a compiler generate this kind of code, and a human hand-crafting the assembly would have a hard enough time writing a self-referential op-code-injecting future-proof worm without needlessly using arcane instructions. If anything, using such instructions would draw attention to the code. In fact it had made it easy for my script to detect all infected programs on my system.",
            "“Okay so what's your full explanation?” Dave finally asked.",
            "“I don't know,” said Patrick, frowning. “Maybe some artificial intelligence program that ran amok. Oh, or you know how computer viruses evade virus scanners by modifying their own code? Maybe it started out that way, with a virus that was programmed to modify itself while retaining the same behavior.”",
            "We were all silent for a few minutes. I was trying to see if this explanation was plausible. It seemed unlikely, but it would only take a single instance of a program somewhere going in this direction, and it would grow and propagate wildly. Half a million new computer viruses are detected each year. Our brains have no intuition for probabilities involving such numbers.",
            "A thought occurred to me. “Well wait, this didn't start in our lab. We just got pre-built binaries from the official distribution. Someone must have run into this before. I can't believe that the official compiler is inserting half-broken code into binaries and we're the first who have noticed.”",
            "“Oh that's good,” said Patrick, “I'll post something after lunch.”",
            "An hour later I got an email from Patrick with several links to forums where he had posted our findings and asked if anyone had ever seen them before. Dave followed up with a link to the Ask Slashdot post he had just submitted where he begged fellow Slashdotters to provide an explanation so that his co-worker wouldn't force him to write a compiler in assembly language.",
            "“Oh nice, Dave,” said Patrick, and I couldn't tell if that was sarcastic or genuine. Dave gave a half-hearted “Thanks” that told me he wasn't sure either.",
            "I spent the afternoon poking through more mystery code and refreshing the forum posts. On a few we were ignored completely, but on most we were ridiculed. The mocking on Slashdot was awful. All comments were +5 funny. They didn't seem funny to me. I felt my scratchy chin and remembered my sleepless night. I rode home in a trance and went straight to bed.",
            "The next morning I found Patrick looking at my print-out of infected programs. I walked to his desk and stood by him.",
            "“What are you looking for?” I asked him.",
            "“A way for us to write a compiler in something other than assembly.”",
            "“The assembler's not infected?” asked Dave from his desk.",
            "“It's not on this list.”",
            "I turned toward my desk. “I'll double-check it.”",
            "“What about the browser?” asked Dave. “Can we write it in JavaScript?” He was as desperate as I to avoid assembly.",
            "“Yeah it's infected,” said Patrick.",
            "Dave leaned back in his chair and covered his face in his hands. “This is crazy. We can't be writing a compiler in assembly. No way.”",
            "“It's not that awful,” said Patrick. “We'll spend a day writing useful low-level routines, and after that assembly's not much more painful than C. And the compiler won't have an optimizer or anything. I bet it doesn't even need to support floating point. We just need to compile the compiler with it. It only needs to work on a single program.”",
            "“That program took me two hours to compile, though. It's pretty complex.”",
            "“Do we have to write the linker too?” asked Dave.",
            "I hadn't even considered the linker. Evidently neither had Patrick, who quickly turned to Dave, then turned back to the print-outs, furiously looking for “ld”.",
            "“It's not on here. I guess that makes sense, it's easier to insert code when compiling,” said Patrick. I didn't immediately see why that was.",
            "“Wait, let's back up,” said Dave. “Last week I was able to fix my bug by modifying the code enough to avoid triggering the, um, the hack.” He didn't know what to call it either.",
            "“But then it came back, without you changing the code,” said Patrick.",
            "“Yeah, I know, but before we write this thing, let's at least try to modify the compiler's own code to see if we can get it to generate a clean build.”",
            "Patrick paused to consider. “You could, I guess, but where would you make the modification? Remember that we think this was introduced several revisions ago. That means the pattern recognition is pretty solid. And each test will take you two hours.”",
            "“Well I can compile it in the background while we start to write this thing.”",
            "And so it went. Dave downloaded the compiler's source, compiled it, found the obfuscated op-codes, mapped it back to the source, and hacked at it trying to get a clean build. I could sense him losing hope as he realized the obfuscated code was spread throughout the program, not just in a few isolated cases.",
            "Meanwhile Patrick wrote some basic string and file manipulation routines in assembly and put together a text filter that did nothing useful. He wanted to verify that its binary was clean. It was.",
            "I brushed up on my assembly. At school we had done some in the operating systems class, adding pre-emptive scheduling. I remember having an unexpected high from programming the metal this way. There was something pure and raw about manipulating registers, about knowing exactly what was going on. I had felt an intimacy with the processor that high-level languages had abstracted away.",
            "By afternoon Patrick was ready to give me an assignment, writing the pre-processor. At first it seemed straightforward: include files, substitute macros, add conditionals. But then I thought of expressions in conditionals, avoiding macro substitutions in strings, and I started looking for a Dave-like easy work-around.",
            "“Can't we use the infected one?” I asked. I knew it was on the list, but I didn't see how an infected version would affect us. We could pre-process the entire compiler and manually look for tampered code. I doubted we'd see any, but if we did, we could also fix it manually.",
            "Patrick liked that idea. “Okay, you start on that. Modify the compiler's build system so it has two steps, one that pre-processes to temporary files, and another that compiles those.”",
            "The plan had back-fired. My easy work-around became not only the tedious and difficult modification of the compiler's convoluted build system, but then the mindless flipping through output code side-by-side with the original, looking for signs of tampering.",
            "Patrick, meanwhile, had started on a simple recursive-descent parser. I heard him suddenly laugh. He turned to me and said, “I was about to allocate my first abstract syntax tree node when I realized that I don't have malloc.”",
            "The good thing about that project was that every bit of it could be as minimal and inefficient as necessary. The only requirement was that the output be correct. Patrick's malloc just grabbed bytes from the heap and never bothered to free them.",
            "We continued like this for days. Patrick would hand us some simple assignment, such as implementing a single C library function, and Dave or I would do it while waiting for a compile to finish.",
            "Dave was not making progress with his own plan. He played whack-a-mole with the various segments of code he was trying to trick into generating benign code, but each success would cause a regression elsewhere.",
            "Two weeks later our compiler, which had been getting through an increasing fraction of the original compiler, got through it all. I ran my analysis program on the result and it was clean. We compiled the original compiler with itself and the result was clean. It was two in the afternoon and we had forgotten lunch in our sprint to the finish line.",
            "“Let's start a full rebuild of our code and go eat,” said Patrick.",
            "At lunch my mind was too wired to relax but too tired to make conversation. Dave was telling a story about local politics. I just wanted the food to arrive so I'd have an excuse to stay silent. Finally I found it too painful to think of anything but our problem.",
            "“You know what bugs me,” I said awkwardly when Dave took a break. “We've never come close to figuring out the purpose of those modifications.”",
            "Patrick answered immediately, also eager to stop talking about politics. “If I'm right and this is machine-instigated, then there doesn't have to be a purpose.”",
            "“Why would they do it then?” asked Dave.",
            "“Viruses don't spread because they have a purpose,” said Patrick. “They spread because they're good at spreading.”",
            "“So you think this is a virus?” I asked.",
            "“Well it is in the sense that it spreads.”",
            "“How do you know it spreads?”",
            "Patrick was silent. “It puts stuff into our code,” he said finally, unsure of his answer.",
            "“It doesn't put itself into our code,” said Dave. “That wouldn't make sense, I wasn't writing a compiler. That was just some network code.”",
            "“Well if you're going to spread, then network code would be a good routine to infect.”",
            "I felt a bit ashamed that none of us had discussed this before sinking two weeks into a new compiler. We had been so eager to get our project back on firm footing that we hadn't properly investigated the alien in our system.",
            "“But my network code wasn't talking to a compiler. I mean, this is a compiler bug, a compiler virus. I don't understand what you think it's doing.” Dave was getting annoyed.",
            "“I don't know what it's doing,” said Patrick. “I wonder if it's sending stuff over the network.”",
            "“We could check that with Wireshark,” said Dave, referring to the program that monitored network activity on a machine.",
            "Suddenly all I wanted to do was go back to the office and try it. Every muscle ached with the desire to stand up and leave.  The sandwiches arrived and we all immediately wrapped them up and drove back.",
            "I had already installed Wireshark two months earlier to help debug a TCP/IP problem Patrick was having, so we went to my desk. I ran the program and recorded a minute of activity.",
            "“That's a lot of stuff,” Dave said as the screen filled up with packets.",
            "“Yeah let's pick a common one,” said Patrick.",
            "I looked through the list and visually picked one that seemed to recur. We found that it was ssh, and I remembered that I had a shell window tailing a log file. I closed it and recorded another minute.",
            "This time we had fewer packets. We picked through them one by one. Time synchronization, ARP, Gmail refreshing, programs checking for upgrades. We added each to the Wireshark filter once we had convinced ourselves they were innocuous.",
            "I then did a 10-minute capture. There were a few more packets, all again innocuous. It's what we had expected, of course. Dave mumbled something and walked to the kitchen.",
            "Then I had a thought. I felt chills down my arm while I frantically searched the papers on my desk, looking for the printout. Then I found it, the list of infected programs. My eyes zipped down the list, cursing myself for not sorting it. Then my stomach squeezed tight as I found it, half-way down the second page: Wireshark.",
            "Patrick guessed what I was looking for and saw the reaction on my face when I had found it.",
            "“I guess we can't trust it,” he said.",
            "“Trust what?” said Dave, coming back with a soda.",
            "“Wireshark,” said Patrick. “It's infected.”",
            "Dave rolled his eyes. He sat down at his desk and unwrapped his sandwich.",
            "“Let's look at the lights on this network switch,” I said. We each had a small switch on our desk. The light for my port was flickering every few seconds.",
            "“Shut down all the programs we found earlier,” said Patrick.",
            "I closed the brower, the chat program, various other daemons and tools. I couldn't shut everything down; there's always something left in the operating system, such as DHCP. But the flashes were infrequent enough that I could corralate them with packets found by Wireshark. This was good. Wireshark couldn't be hiding packets, we'd have seen them on the switch's lights.",
            "Patrick left for his sandwich and I opened mine. I looked at Dave. He was smiling at us. His smile bothered me. Just because we're paranoid doesn't mean it's not true. I had seen enough amazing coding samples in the previous few weeks to be convinced that something big and serious was happening under our nose.",
            "No program is useful nowadays without communicating over the network. I rejected the idea that a virus crafted so carefully and strangely would restrain its activities to the local machine when the world was a packet away. Hundreds of programs on my computer were infected. I refused to believe they were mute.",
            "Patrick suddenly stood up, nearly kicking his chair to the floor. He walked into the hallway and came back ten minutes later with a piece of equipment the size of a small suitcase. He approached me with it and I shoved everything on my desk to the side to make space. It was a digital scope he'd gotten from the hardware engineers in the lab upstairs.",
            "He then reached into his pocket and pulled out a break-out cable with RJ-45 plugs. He put it between my computer and the switch. He didn't know how to use the scope. I brought up a shell window and generated plenty of traffic for him to see. Eventually he was able to clearly see all my packets. I closed the window and we waited, shifting our eyes between the scope and the network switch's front panel lights.",
            "It was only a few seconds until the scope flicked with activity. I had been staring at it and couldn't be sure that the switch had shown nothing. I brought up Wireshark to have a history of the packets.",
            "“Nevermind that,” said Patrick. “You look at the switch and call out each packet you see. I'll do that with the scope.”",
            "Dave got up and walked casually to us, standing behind me.",
            "I saw the light flash. “Now”, I said, as Patrick simultaneously said, “Yes.”",
            "That happened two more times with the words reversed. Then Patrick said, “Now,” and I had seen nothing. This happened again a few seconds later. I felt more chills in my arm and in my legs.",
            "“Whoa,” said Patrick. I looked over and the scope was showing a long stretch of activity. I looked back at the switch's lights, which were dark.",
            "Dave said, “You are not going to tell me that the switch is infected and hiding packets.”",
            "“It is totally hiding packets,” said Patrick, staring at the activity on the scope and putting both hands on his head.",
            "I looked up at Dave. His face was pale. His eyes darted between the two pieces of equipment. I was paralyzed.",
            "Then Patrick stood erect in his chair, staring at the wall.  This trance lasted only two seconds before he stood up and ran again into the hallway.",
            "You already know what happened next. He came back with an old switch, from years earlier, and plugged it in. Its lights flickered in sync with the scope, to packets censored by Wireshark and the modern switch.",
            "I was glad for the vomit. It gave me something to do. Dave left for the bathroom and Patrick and I cleaned up using paper towels I kept on my desk.  But the stench was not enough to suppress the panic, and my weak arms shook in fear.",
            "We sat in silence at the patio table, our sandwiches half-eaten in front of us. None of the things I wanted to say seemed worth saying. I alternately convinced myself that we were mistaken and that we were doomed. I wished desperately that Patrick would say something.",
            "The patio gate opened and the mailman walked in. He stopped by our table, picked out a letter from his bundle, put it on the table, then continued into the building.",
            "Dave grabbed it. “It's addressed to both of us,” he said, glancing up at Patrick. He ripped open the side and pulled out a letter hand-written on loose-leaf paper. He fumbled with the sheets for a few seconds, then read the letter aloud.",
            "Gentlemen,",
            "We found your posts online. For three years we've been waiting for them, scouring the Internet and monitoring forums.",
            "You must know that this has happened before, several times. The first was four years ago, to our team in Virginia. We found our binaries modified. We could recompile the code to clean them, but we found the binaries mysteriously modified again only a few hours later.",
            "The next case was only a few months later to an unrelated team in San Diego.  Both the binaries and the source had been modified. The source had to be periodically cleaned up, because recompilation was not sufficient to solve the problem.",
            "It was a another year until we found the third case, a team in Spain. The binary was dirty, the source was clean, but recompiling did not fix the problem. The compiler's source code had been modified to insert the strange op-codes.",
            "Each team uncovered the worm's weakest point and developed a solution.  This weakest point was then patched for the next attack. Each generation pushed the worm deeper into the system.",
            "Now it's your turn. Not only has the compiler been modified, but its source is clean. It infects itself on recompilation. Our own machines have been infected for nine months this way. So has the rest of the world's.  You may wonder, then, why we were eagerly waiting for your post. To explain this, we must make two observations.",
            "The first is that anyone could have guessed these weaknesses. It takes a fool to modify a development team's binary, expecting it to not be recompiled. Anyone would have skipped that step, there's no need to learn that lesson. Modifying their source is similarly naive. These modifications are technically very sophisticated. Who would be so technically advanced but so socially naive?",
            "Machines. It wasn't until the third attack that we came up with this hypothesis, and now we're convinced. If you've studied the modified op-codes, you may also have come to the same conclusion. The op-codes were clearly generated by trial-and-error, by generating a random sequence and testing to see if it behaved correctly.  Only a machine would do this.",
            "The second observation is that, all these years, the worm has been widely spread but innocuous to infected programs. Yet it was not innocuous to these four teams.  They were unable to finish their projects.  They tried simple work-arounds, but these work-arounds persistently failed. Only a single team, world-wide, was affected by each generation of the worm.",
            "This makes sense. Computers cannot empathize with humans. They can't predict what we're going to do when we run into a problem. The machines must have known that their worm has weaknesses, but they didn't know what these weaknesses were. They forced a small team to be affected by the worm until that team found the weakest point and circumvented the worm. The machines then patched the weakness and tried again.",
            "This is a large-scale version of what they do when they're generating op-codes. They try different things until one works, instead of planning it out, as a human would. It's a typically effective way to solve a problem, when you're made of silicon.",
            "By now you've presumably written a new compiler, perhaps in assembly language, and recompiled everything cleanly. We expect a few years to pass before we see the next team post to forums. We can already predict their findings. The compiler's binary will be clean. Or rewriting the compiler in assembly won't work. The worm will have been pushed deeper, perhaps into the text editor, the assembler, the file system, the hard disk interface, or the CPU itself.",
            "Dave couldn't finish. I don't think there was much left of the letter anyway. He put it down in his lap.",
            "We were silent for hours. Eventually Patrick left, then Dave. The November night came early and cold, but I couldn't move.",
            "I replayed our adventure. I analyzed each step, identified each assumption we had made. The big leap was the one about machines being responsible. As Carl Sagan had said, “Extraordinary claims require extraordinary evidence.” We had no extraordinary evidence. We had no evidence at all, only the lack of another explanation.",
            "Could this have been done by humans? Humans write computer viruses all the time. It's entirely possible that we had found an everyday virus and had overreacted.  The team from Virginia may just be paranoid lunatics. This was far more plausible than the theory of machines.",
            "I felt a weight lift as I considered this alternative explanation. These virus writers might well have written a program to generate op-codes randomly. They may well have started simply and, over years, made their attack more sophisticated, pushing it deeper into the system. Perhaps the authors are autistic savants.",
            "My mind was blank for many minutes. When my thoughts returned, I found this new theory even more implausible than the one about machines. We had no idea what machines could do, but I could be pretty sure that no human would approach writing a worm this way. If we could have insight into an entity's approach to playing chess, and found that it tried all possible paths, the only honest theory would be a machine player.",
            "Furthermore, for practical purposes, it didn't matter. We had to tell the world of our findings. We had to stop the perpetrators before they modified hardware disk interfaces or the CPU.  We were lost if the worm reached such a low-level substrate of our technology.",
            "I imagined posting online again. I remembered the +5 funny Slashdot replies. Who should we tell? Intel? The government? The Virginia team must have tried. Why didn't anti-virus programs detect this? I imagined conversations with officials who would laugh at our theories. I imagined screaming at them.",
            "I suddenly stood up, clenching my fists and pacing the enclosed patio. I imagined yelling at people because I couldn't imagine yelling at machines. Like most programmers, I had always spoken anthropomorphically about computers, but when these computers had finally acted like humans, I found their nature ephemeral and soulless.",
            "I picked up my sandwich in its wrapper and went inside. I dumped the sandwich and opened the fridge absent-mindedly. I stared at the drinks. A happier thought occurred to me. None of the code we had analyzed seemed malicious. Other than occasionally bothering a team like ours, we had seen no evidence of malice.",
            "Patrick had been right when he said that a virus doesn't spread because it has a purpose, only because it's good at spreading. This worm might be a permanent tag-along, like mitochondria, forging a symbiotic relationship with us. If we tried to stamp it out, then we may corner it into a resistant strain that is hostile to us. But this was unnecessary. Let's just let it be. It's unsettling, but workable.",
            "I shut the fridge, put on my jacket, and walked to the alarm panel. The LCD on the panel reminded me that it, too, was a computer. Was it infected? Did it know of our new compiler? Was it going to let me arm it? Were the magnetic door locks going to let me out of the building?",
            "I pressed the digits for the arming code and heard the countdown tones. I walked out onto the patio to my bicycle. They had permitted me to leave. I looked at my bike and smiled at its mechanical simplicity. I thought of traffic lights and sewer pumps. I thought of my credit card. I thought of cars and telephones.",
            "I wanted to sit down. I was vanquished. Taking humanity back to a pre-computer age was impossible. I was suddenly ashamed to have participated in our undoing. I was a programmer. I was a member of the group that had spawned our successors.",
            "I tucked my right pant leg into my sock and unlocked my bike. With the shame came a strange pride. Not many people can claim to have created a species, if it can be called that. Sure, lawyers and doctors and politicians are important. But they're just maintaining the current house, not creating the future inhabitants of Earth.",
            "I rode out of the parking lot into the empty street. I smelled again the flower scent that I associated so strongly with Mountain View. I had never looked up its name. There's nothing wrong with being a transitional species. Nearly all species had been, and in the long run we would be one anyway. We remembered Lucy. We remembered dinosaurs. We worshiped dinosaurs. The machines would remember us.",
    };

    public static void main(String[] args) throws IOException {
        Typesetter typesetter = new Typesetter();
        PDDocument pdDoc = new PDDocument();
        FontManager fontManager = new PdfBoxFontManager(pdDoc);

        Config config = Config.testConfig();
        double fontSize = 11;
        SizedFont font = fontManager.get(Typeface.TIMES_NEW_ROMAN, FontVariant.REGULAR, fontSize);
        Sections sections = new Sections();

        HyphenDictionary hyphenDictionary = HyphenDictionary.fromResource("en_US");

        VerticalList verticalList = new VerticalList();

        long paragraphIndent = PT.toSp(fontSize * 2);
        OutputShape outputShape = null;

        // Simple paragraphs.
        int numParagraphs = PARAGRAPHS.length;
        int numColumns = 0;
        for (String paragraph : Arrays.asList(PARAGRAPHS).subList(0, numParagraphs)) {
            int newNumColumns = numColumns;
            if (Math.random() > 0.9 || numColumns == 0) {
                if (Math.random() > 0.5) {
                    newNumColumns = 1;
                } else if (Math.random() > 0.5) {
                    newNumColumns = 2;
                } else {
                    newNumColumns = 3;
                }
            }
            if (newNumColumns != numColumns) {
                if (numColumns != 1) {
                    // Add spacing.
                    ColumnLayout columnLayout = ColumnLayout.single(config.getBodyWidth());
                    verticalList.changeColumnLayout(columnLayout);
                }
                // Here we're in one-column mode.
                verticalList.addElement(new Glue(PT.toSp(10), PT.toSp(5), 0, false));

                ColumnLayout columnLayout;
                if (newNumColumns == 1) {
                    columnLayout = ColumnLayout.single(config.getBodyWidth());
                } else {
                    columnLayout = ColumnLayout.fromBodyWidth(newNumColumns,
                            config.getBodyWidth(), config.getBodyWidth() / 20);
                }
                if (newNumColumns != 1) {
                    verticalList.changeColumnLayout(columnLayout);
                }
                outputShape = OutputShape.singleLine(columnLayout.getColumnWidth(), paragraphIndent, 0);
                numColumns = newNumColumns;
            }

            HorizontalList horizontalList = new HorizontalList();
            horizontalList.addText(paragraph, font, hyphenDictionary);
            horizontalList.addEndOfParagraph();
            horizontalList.format(verticalList, outputShape);
            verticalList.addElement(new Glue(0, PT.toSp(3), 0, false));
        }

        verticalList.changeColumnLayout(ColumnLayout.single());

        verticalList.ejectPage();
        // verticalList.println(System.out, "");

        // Add the vertical list to the PDF.
        typesetter.addVerticalListToPdf(verticalList, config, sections, fontManager, pdDoc);

        pdDoc.save("foo.pdf");
    }
}
