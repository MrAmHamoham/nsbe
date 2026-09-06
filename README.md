# NSBE

Makes Block Entities use Baked Models rather than Entity Models. + Other related optimizations

---

## Targets
- Banner Posts[^banner]
- Beacons[^beacon]
- Chests[^chest]
- Ender Chests[^chest]
- Signs[^sign]
- Skulls[^skull]
- Trapped Chests[^chest]

[^banner]: Only the post is baked.

[^beacon]: Skips beams when inactive.

[^chest]: Only the static chest is baked.

[^sign]: Except for signs with text.

[^skull]: Except for player heads.
