package com.example.piyush.magicalmethods.lib

import android.content.Context
import java.lang.Math.pow
import java.util.*

/**
 * Created by ashish kumar on 28-12-2017 | 06:05 PM.
 */

/**
 * DHKeyExchange
 */
class DHKeyExchange(_context: Context) {
    private val context = _context

    private val gen = 213213.0
    private val mod = 507

    private var modA = 0
    private var a = 0

    /**
     * Generates a random string
     */
    fun generateModA(): Int {
        a = generateRandomInt()
        modA = generateMod(gen, mod, a)    // (Math.pow(gen, a.toDouble()) % mod).toInt()

        return modA
    }

    fun generateKey(modB: Int): String {
        val key = generateMod(modB.toDouble(), mod, a)                         // (Math.pow(modB.toDouble(), a.toDouble()) % mod).toInt()
        println(key)
        val hash = Util.md5(key.toString())
        return hash
    }

//    private fun toBinaryArray(_number: Int): List<Int> {
//        var number = _number
//        val bits = arrayListOf<Int>()
//
//        while (number > 0) {
//            bits.add(number % 2)
//            number %= 2
//        }
//
//        bits.reverse()
//        return bits
//    }
//
//    fun calcVal() {
//        val bits = toBinaryArray(gen.toInt())
//
//        var base = 1
//        var power = 0
//        for (bit in bits) {
//            power += bit * base
//            base *= 2
//        }
//    }

    /**
     * @source https://math.stackexchange.com/questions/2204627/repeated-squaring-techniques
     */
    private fun generateMod(gen: Double, mod: Int, _num: Int) : Int {
        var num = _num
        var modNum = 0

        val sequence = arrayListOf<Double>()
        while (num > 0) {
            sequence.add(num.toDouble())
            num /= 2
        }

        sequence.reverse()
        var i = -1

        while (++i < sequence.size) {
            modNum = if (i > 0) {
                val remainder = sequence[i] % sequence[i - 1]
                val temp = modNum * modNum * pow(gen, remainder).toInt()
                temp % mod
            } else {
                pow(gen, sequence[i]).toInt() % mod
            }
        }
        return modNum
    }

    private fun generateRandomInt(): Int {
        val random = Random()

        val from = 10
        val to = 343

        return random.nextInt(to - from) + from
    }
}